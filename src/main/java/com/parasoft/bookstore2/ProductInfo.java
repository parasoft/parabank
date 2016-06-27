package com.parasoft.bookstore2;

import java.io.*;
import java.math.*;

public class ProductInfo implements Serializable
{
    /**
     * <DL><DT>serialVersionUID</DT> <DD>Default serial version UID</DD></DL>
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String title;
    private int quantity_in_stock;
    private BigDecimal amount;

    public ProductInfo() {
        // for serialization only
    }
    
    protected ProductInfo(int id, String name, BigDecimal amount, int quantity)
        throws ItemNotFoundException {
        this.id = id;
        this.title = name;
        this.amount = amount;
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
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public void inflateAmount(BigDecimal inflate) {
        setAmount(amount.add(inflate)); 
    }
}
