package com.parasoft.bookstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO add description</DD>
 * <DT>Date:</DT>
 * <DD>Oct 6, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 * @req PAR-39
 */

public class CartServiceTest extends AbstractCartService {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CartServiceTest.class);

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#addItemToCart(java.lang.Integer, int, int)}
     * .
     *
     * @req PAR-1
     */
    @Test
    public void testAddItemToCart() {
        DisplayOrder no = null;
        int cartId = getCurrentCartId();
        // add cart with negative quantity
        try {
            no = getService().addItemToCart(0, 0, -1);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Cannot have an order with negative quantity.");
        }
        assertNull(no);
        try {
            // add order to cart with id 0
            no = getService().addItemToCart(0, 1, 1);
            int newCartId = no.getCartId();
            getCurrentCartId(); // just logging the the new cart
            assertEquals(cartId + 1, newCartId);
            Book book = no.getItem().getBook();
            assertEquals(book.getId(), 1);
            assertEquals(book.getStockQuantity(), 20);
            assertEquals(book.getPublisher(), "Prentice Hall");
            // add order to existing cart (making it two items)
            no = getService().addItemToCart(newCartId, 1, 1);
            assertEquals(no.getCartId(), newCartId);
            Order order = no.getItem();
            assertEquals(order.getQuantity(), 2);
            // add order to non-existent cart
            no = getService().addItemToCart(1000, 1, 1);
        } catch (Exception e) {
            if (!e.getMessage().equals("An order with Cart Id 1000 does not exist!")) {
                log.error("got unexpected exception ", e);
            }
            assertEquals("An order with Cart Id 1000 does not exist!", e.getMessage());
        } finally {
            getCurrentCartId();
        }
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#updateItemInCart(int, int, int)}
     * .
     * @req PAR-1
     */
    @Test
    public void testUpdateItemInCart() {
        DisplayOrder no = null;
        int cartId = getCurrentCartId();
        // update order with negative quantity
        int newCartId = 0;
        try {
            no = getService().addItemToCart(0, 1, 1);
            newCartId = no.getCartId();
            assertEquals(newCartId, cartId + 1);
            getCurrentCartId();
            no = null;
            no = getService().updateItemInCart(newCartId, 1, -1);
        } catch (Exception e) {
            assertEquals("Cannot update an order with negative quantity.", e.getMessage());
        }
        assertNull(no);
        // update order with higher quantity than what's in stock
        try {
            // add order to existing cart
            no = getService().addItemToCart(newCartId, 1, 1);
            assertEquals(newCartId, no.getCartId());
            Order order = no.getItem();
            assertEquals(order.getQuantity(), 2);
            no = null;
            no = getService().updateItemInCart(newCartId, 1, 1000);
        } catch (Exception e) {
            assertEquals("Did not update order with cartId " + newCartId + ","
                + " 1000 is greater than the quantity in stock: 20", e.getMessage());
        }
        assertNull(no);
        getCurrentCartId();
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#getItemByTitle(java.lang.String)}
     * .
     * @req PAR-1
     */
    @Test
    public void testGetItemByTitle() {
        log.info("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#getItemById(int)}.
     */
    @Test
    public void testGetItemById() {
        log.info("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#addNewItemToInventory(com.parasoft.bookstore.Book)}
     * .
     */
    @Test
    public void testAddNewItemToInventory() {
        log.info("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#submitOrder(int)}.
     * @req PAR-1
     */
    @Test
    public void testSubmitOrder() {
        log.info("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#getItemsInCart(int)}.
     */
    @Test
    public void testGetItemsInCart() {
        log.info("Not yet implemented");
    }

    /**
     * Test method for
     * {@link com.parasoft.bookstore.CartService#removeExpiredOrdersAndBooks()}.
     */
    @Test
    public void testRemoveExpiredOrdersAndBooks() {
        log.info("Not yet implemented");
    }

}
