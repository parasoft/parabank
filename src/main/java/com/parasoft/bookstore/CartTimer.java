package com.parasoft.bookstore;

// public class CartTimer extends TimerTask {
public class CartTimer implements Runnable {
    @Override
    public void run() {
        new CartService().removeExpiredOrdersAndBooks();
    }
}
