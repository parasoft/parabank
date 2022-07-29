package com.parasoft.parabank.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a customer's bank account
 */
@XmlRootElement(name = "account")
@XmlType(propOrder = { "id", "customerId", "type", "balance" })
public class Account {

    public enum AccountType {
        CHECKING(false), SAVINGS(false), LOAN(true);

        private final boolean internal;

        private AccountType(final boolean internal) {
            this.internal = internal;
        }

        public boolean isInternal() {
            return internal;
        }
    }

    private int id;

    private int customerId;

    private AccountType type;

    private BigDecimal balance;

    public void credit(final BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void debit(final BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        final Account other = (Account) obj;
        return id == other.id && customerId == other.customerId && type == other.type
            && Util.equals(balance, other.balance);
    }

    @XmlTransient
    @JsonIgnore
    public BigDecimal getAvailableBalance() {
        return balance.signum() < 0 ? new BigDecimal(0) : balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getId() {
        return id;
    }

    @XmlTransient
    @JsonIgnore
    public int getIntType() {
        if (type == null) {
            return AccountType.LOAN.ordinal();
        }
        return type.ordinal();
    }

    public AccountType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + customerId;
        result = prime * result + (type == null ? 0 : type.hashCode());
        result = prime * result + (balance == null ? 0 : balance.hashCode());
        return result;
    }

    public void setBalance(final BigDecimal balance) {
        this.balance = balance;
    }

    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @JsonIgnore
    public void setIntType(final int type) {
        this.type = AccountType.values()[type];
    }

    public void setType(final AccountType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", customerId=" + customerId + ", type=" + type + ", balance=" + balance + "]";
    }
}
