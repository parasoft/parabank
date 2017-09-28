package com.parasoft.bookstore;

public class DisplayOrder {
    private int cartId;
    private Order item;

    public DisplayOrder() {
        //
    }

    public DisplayOrder(Order order, int cartId) {
        setItem(order);
        setCartId(cartId);
    }

    public Order getItem() {
        return item;
    }

    public void setItem(Order item) {
        this.item = item;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
