package com.parasoft.parabank.web.form;

import java.math.BigDecimal;

/**
 * Backing class for loan request form
 */
public class RequestLoanForm {
    private BigDecimal amount;
    private BigDecimal downPayment;
    private int fromAccountId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
}
