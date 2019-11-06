package com.parasoft.bookstore2;

import java.util.TimerTask;

public class CartTimer extends TimerTask {
    @Override
    public void run() {
        new CartService().removeExpiredOrdersAndBooks();
    }
}
