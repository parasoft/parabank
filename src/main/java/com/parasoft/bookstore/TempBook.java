package com.parasoft.bookstore;

public class TempBook {
    private Book book;
    private long timestamp;

    public TempBook() {
        //
    }

    public TempBook(Book book, long timestamp) {
        this.book = book;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void refreshTimestamp() {
        timestamp = System.currentTimeMillis();
    }
}