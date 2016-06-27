package com.parasoft.bookstore2;

import java.util.*;

public class CartTimer extends TimerTask {
    public void run() {
        new CartService().removeExpiredOrdersAndBooks();
    }
}
