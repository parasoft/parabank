package com.parasoft.bookstore2;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Book implements Serializable{
    /**
     * <DL><DT>serialVersionUID</DT> <DD>Default serial version UID</DD></DL>
     */
    private static final long serialVersionUID = 1L;
    protected String isbn;
    protected String genre;
    protected Date publication_date;
    protected String description;
    protected String[] authors;
    protected String publisher;
    protected long timestamp;
    protected ProductInfo product;

    public Book() {
        // for serialization only
    }

    public Book(String isbn, String genre, Date year, String[] authors,
                   String publisher, String description, ProductInfo product)
        throws ItemNotFoundException
    {
        this.product = product;
        this.isbn = isbn;
        this.genre = genre;
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
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

    public ProductInfo getProductInfo(){
        return product;
    }

    public void setProductInfo(ProductInfo product){
        this.product = product;
    }

    public void inflatePrice(BigDecimal bigDecimal) {
        product.inflateAmount(bigDecimal);
    }
}