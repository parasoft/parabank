package com.parasoft.bookstore2;

import java.util.Date;

public class Order {
    private Book book;
    private int quantity;
    private long timestamp;

    public Order() {
        this(null, 0, System.currentTimeMillis());
    }

    public Order(Book book, int quantity, long timestamp) {
        this.book = book;
        this.quantity = quantity;
        this.timestamp = timestamp;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void modifyCount(int amount) {
        quantity += amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void refreshTimestamp() {
        timestamp = System.currentTimeMillis();
    }

    public String getDescription() {
        return "Order: " + book.getProductInfo() + " x" + quantity + " on " + new Date(timestamp).toString();
    }
}