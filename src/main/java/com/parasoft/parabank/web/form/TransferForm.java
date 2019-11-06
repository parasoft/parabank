package com.parasoft.parabank.web.form;

import java.math.BigDecimal;

/**
 * Backing class for account transfer form
 */
public class TransferForm {
    private BigDecimal amount;
    private int fromAccountId;
    private int toAccountId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public int getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(int toAccountId) {
        this.toAccountId = toAccountId;
    }
}
