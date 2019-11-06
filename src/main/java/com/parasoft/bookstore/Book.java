package com.parasoft.bookstore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Book extends Item implements Serializable{
    /**
     * <DL><DT>serialVersionUID</DT> <DD>Default serial version UID</DD></DL>
     */
    private static final long serialVersionUID = 1L;
    protected String isbn;
    protected Date publication_date;
    protected String description;
    protected String[] authors;
    protected String publisher;
    protected long timestamp;

    public Book() {
        // for serialization only
    }

    protected Book(int id, String isbn, String title, Date year, String[] authors,
                   String publisher, String description, BigDecimal price, int stock)
        throws ItemNotFoundException
    {
        super(id, title, price, stock);
        this.isbn = isbn;
        publication_date = year;
        this.authors = authors;
        this.publisher = publisher;
        this.description = description;
    }

    public String getISBN() {
        return isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublicationDate() {
        return publication_date;
    }

    public void setPublicationDate(Date publication_date) {
        this.publication_date = publication_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void refreshTimestamp() {
        timestamp = System.currentTimeMillis();
    }
}