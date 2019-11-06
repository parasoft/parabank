package com.parasoft.bookstore;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * Java interface for the Book store web service
 */
@WebService(name="Bookstore")
public interface ICartService {
    /**
     * Adds a specified amount of Books to a cart. If no cartId is given or if
     * the cartId is equal to 0, a unique Id will be generated for the user.
     * All carts that are inactive for 20 minutes will be removed.
     *
     * @param cartId id of the cart where the Order should be placed
     * @param itemId id of the Book to add
     * @param quantity amount of books to add
     * @return Returns an "Order" object, which includes a Book object,
     * quantity and cart id.
     * @throws Exception
     */
    @WebResult(name="itemAdded")
    DisplayOrder addItemToCart(
        @WebParam(name="cartId") Integer cartId,
        @WebParam(name="itemId") int itemId,
        @WebParam(name="quantity") int quantity
    ) throws Exception;

    /**
     * Update an existing order in a cart. Cannot update an order where quantity
     * supplied is greater than the quantity in stock. Possible to update an
     * order to have zero quantity.
     * @param cartId id of the cart to modify
     * @param itemId id of the Book to modify
     * @param quantity changes the amount of Books in the Order
     * @return Returns an updated "Order" object.
     * @throws Exception
     */
    @WebResult(name="updatedItem")
    DisplayOrder updateItemInCart(
        @WebParam(name="cartId") int cartId,
        @WebParam(name="itemId") int itemId,
        @WebParam(name="quantity") int quantity
    ) throws Exception;

    /**
     * Search for a book based on the given keyword, which is case-insensitive.
     * Example keywords: linux, c++, java
     *
     * @param titleKeyword keyword of a book's title to search against
     * @return a list of Book objects that match the title keyword. The price
     * of the Books are increased by $1.00 every 5 invocations.
     * @throws Exception
     */
    @WebResult(name="book")
    Book[] getItemByTitle(
        @WebParam(name="titleKeyword") String titleKeyword
    ) throws Exception;

    /**
     * Search for a book based on a given id. Current valid values are 1-9.
     *
     * @param id id of the book to search for
     * @return a single Book object that matches the specified id.
     * @throws Exception
     */
    @WebResult(name="book")
    Book getItemById(
        @WebParam(name="id") int id
    ) throws Exception;

    /**
     * Add new book entries into the database. New entries will not be added to
     * the permanent database and will be removed after 20 minutes of inactivity.
     *
     * @param book Book object that will be added to the database
     * @return Book object added to the database
     * @throws Exception
     */
    @WebResult(name="book")
    Book addNewItemToInventory(
        @WebParam(name="book") Book book
    ) throws Exception;

    /**
     * Submits the items in a cart. This removes all the existing items
     * in the cart and deletes the cartId.
     *
     * @param cartId id of the cart to submit Orders from
     * @return true if Orders in the specified cartId were submitted, false
     * otherwise
     */
    @WebResult(name="orderInformation")
    SubmittedOrder submitOrder(
        @WebParam(name="cartId") int cartId
    );

    /**
     * Returns a list of items that exist in the specified cart.
     *
     * @param cartId id of the cart to get items from
     * @return A list of Orders that exist in a given cart
     * @throws Exception
     */
    @WebResult(name="cart")
    CartManager getItemsInCart(
        @WebParam(name="cartId") int cartId
    ) throws Exception;
}