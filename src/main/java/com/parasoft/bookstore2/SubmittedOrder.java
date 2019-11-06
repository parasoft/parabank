package com.parasoft.bookstore2;

import java.util.Date;

public class SubmittedOrder {
    private boolean successIndicator;
    private Date date;

    public SubmittedOrder() {
        //default implementation
    }

    public SubmittedOrder(boolean successIndicator, long time) {
        this.successIndicator = successIndicator;
        date = new Date(time);
    }

    public void setOrderTime(Date date) {
        this.date = date;
    }

    public Date getOrderTime() {
        return date;
    }

    public void setSuccess(boolean successIndicator) {
        this.successIndicator = successIndicator;
    }

    public boolean getSuccess() {
        return successIndicator;
    }
}
