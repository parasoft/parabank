package com.parasoft.parabank.web.form;

import com.parasoft.parabank.domain.TransactionCriteria;

/**
 * Backing class for transaction search form
 */
public class FindTransactionForm {
    private int accountId;
    private TransactionCriteria criteria;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public TransactionCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(TransactionCriteria criteria) {
        this.criteria = criteria;
    }
}