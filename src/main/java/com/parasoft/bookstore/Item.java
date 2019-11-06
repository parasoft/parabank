package com.parasoft.bookstore;

import java.io.Serializable;
import java.math.BigDecimal;

public class Item implements Serializable {
    /**
     * <DL><DT>serialVersionUID</DT> <DD>Default serial version UID</DD></DL>
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private int quantity_in_stock;
    private BigDecimal price;

    public Item() {
        // for serialization only
    }

    public Item(int id, String name, BigDecimal price, int quantity)
        throws ItemNotFoundException {
        this.id = id;
        title = name;
        this.price = price;
        quantity_in_stock = quantity;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStockQuantity() {
        return quantity_in_stock;
    }

    public void setStockQuantity(int quantity_in_stock) {
        this.quantity_in_stock = quantity_in_stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void inflatePrice(BigDecimal amount) {
        setPrice(price.add(amount));
    }
}