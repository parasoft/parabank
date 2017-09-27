package com.parasoft.bookstore2;

public class DisplayOrder {
    private int cartId;
    private Order item;

    public DisplayOrder() {
        //
    }

    public DisplayOrder(Order order, int cartId) {
        item = order;
        this.cartId = cartId;
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
