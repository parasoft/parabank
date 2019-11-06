package com.parasoft.parabank.domain;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Command object containing search criteria for transactions
 *
 * The SearchType value determines which parameters are used for the search
 */
public class TransactionCriteria {
    public enum SearchType {
        ACTIVITY, ID, DATE, DATE_RANGE, AMOUNT;
    }

    // ACTIVITY
    private String month;
    private String transactionType;

    // ID
    private Integer transactionId;

    // DATE
    private Date onDate;

    // DATE_RANGE
    private Date fromDate;
    private Date toDate;

    // AMOUNT
    private BigDecimal amount;

    private SearchType searchType;

    public static final ThreadLocal<DateFormat> DATE_FORMATTER = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("MM-dd-yyyy");
        }
    };


    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Date getOnDate() {
        return onDate;
    }

    public void setOnDate(Date onDate) {
        this.onDate = onDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}
