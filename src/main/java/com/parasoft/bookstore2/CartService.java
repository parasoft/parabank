package com.parasoft.bookstore2;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jws.WebService;

/*
 * Bookstore Web Service implementation
 */
@WebService(
        endpointInterface = "com.parasoft.bookstore2.ICartService",
        serviceName = "Bookstore2")
public class CartService implements ICartService {
    private static final long timeoutInMilliseconds = 1200000; // 20 minutes
    private static final Map<Integer, TempBook> addedBookIds =
        Collections.synchronizedMap(new ConcurrentHashMap<Integer, TempBook>());
    private final CartManager cart = new CartManager();
    private int invocationCounter = 0;

    /**
     * <DL><DT>Description:</DT><DD>
     * This method is used for testing, this way we can 'guess' the next cart id
     * </DD>
     * <DT>Date:</DT><DD>Oct 7, 2015</DD>
     * </DL>
     * @return the current highest cart id
     * @see com.parasoft.bookstore2.CartManager#getStaticCart_Id()
     */
    public int getStaticCart_Id() {
        return cart.getStaticCart_Id();
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#addItemToCart(int, int, int)
     */
    @Override
    public DisplayOrder addItemToCart(Integer cartId, int itemId, int quantity)
        throws Exception
    {
        if (quantity < 0) {
            throw new Exception("Cannot have an order with negative quantity.");
        }

        Order newOrder = new Order(BookStoreDB.getById(itemId),
                quantity, System.currentTimeMillis());

        if (cartId == null || cartId <= 0) {
            cart.addNewItemToCart(newOrder);
            return new DisplayOrder(newOrder, cart.getStaticCart_Id());
        }

        return new DisplayOrder(cart.addExistingItemToCart(cartId, newOrder), cartId);
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#updateItemInCart(int, int, int)
     */
    @Override
    public DisplayOrder updateItemInCart(int cartId, int itemId, int quantity)
        throws Exception
    {
        if (quantity < 0) {
            throw new Exception("Cannot update an order with negative quantity.");
        }
        if (cart.getCartSize() == 0) {
            throw new Exception("Did not update order with cartId " + cartId +
                   ", no orders were submitted.");
        }

        return new DisplayOrder(
                cart.updateExistingItem(cartId, itemId, quantity), cartId);
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#getItemByTitle(java.lang.String)
     */
    @Override
    public Book[] getItemByTitle(String title) throws Exception {
        ++invocationCounter;
        Book[] books = BookStoreDB.getByTitleLike(title != null? title : "");
        for (Book b : books) {
            b.inflatePrice(new BigDecimal(invocationCounter/5));
        }
        return books;
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#getItemById(int)
     */
    @Override
    public Book getItemByIdentifier(int id) throws Exception {
        return BookStoreDB.getById(id);
    }


    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#addNewItemToInventory(com.parasoft.parabank.store.Book)
     */
    @Override
    public synchronized Book addNewItemToInventory(Book book) throws Exception {
        Book existing = null;
        try {
            existing = getItemByIdentifier(book.getProductInfo().getId());
        } catch (Exception e) {
            TempBook tb = new TempBook(book, System.currentTimeMillis());
            addedBookIds.put(new Integer(book.getProductInfo().getId()), tb);
            BookStoreDB.addNewItem(tb);
        }
        if (existing != null) {
            throw new Exception("An item with ID=" + book.getProductInfo().getId() +
                    " already exists and it has the title: " +
                    existing.getProductInfo().getName());
        }
        return book;
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#submitOrder(int)
     */
    @Override
    public SubmittedOrder submitOrder(int cartId) {
        return new SubmittedOrder(cart.removeOrder(cartId),
                                  System.currentTimeMillis());
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.store.ICart#getItemsInCart(int)
     */
    @Override
    public CartManager getItemsInCart(int cartId) throws Exception {
        return new CartManager(cartId);
    }

    public void removeExpiredOrdersAndBooks() {
        synchronized (cart) {
            Collection<List<Order>> list = cart.getCart().values();
            Iterator<List<Order>> iterator = list.iterator();
            while (iterator.hasNext()) {
                Iterator<Order> itr = iterator.next().iterator();
                while (itr.hasNext()) {
                    Order order = itr.next();
                    long difference =
                        System.currentTimeMillis() - order.getTimestamp();
                    if (difference > timeoutInMilliseconds) {
                        itr.remove();
                    }
                }
            }
            cart.removeEmptyMappings();
        }
        synchronized (addedBookIds) {
            Iterator<Map.Entry<Integer, TempBook>> iterator =
                addedBookIds.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, TempBook> entry = iterator.next();
                long difference =
                    System.currentTimeMillis() - entry.getValue().getTimestamp();
                if (difference > timeoutInMilliseconds) {
                    BookStoreDB.clearAddedBooks(entry.getValue());
                    iterator.remove();
                }
            }
        }
    }
}