package com.parasoft.parabank.web.form;

import java.math.BigDecimal;

import com.parasoft.parabank.domain.Payee;

/**
 * Backing class for online bill pay form
 */
public class BillPayForm {
    private Payee payee;
    private Integer verifyAccount;
    private BigDecimal amount;
    private int fromAccountId;

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public Integer getVerifyAccount() {
        return verifyAccount;
    }

    public void setVerifyAccount(Integer verifyAccount) {
        this.verifyAccount = verifyAccount;
    }

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
}
