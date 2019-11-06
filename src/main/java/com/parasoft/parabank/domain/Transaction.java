package com.parasoft.parabank.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.google.gson.JsonObject;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.DateTimeAdapter;
import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a bank account transaction
 */
@XmlRootElement(name = Constants.TRANSACTION)
@XmlType(propOrder = { "id", "accountId", "type", "date", "amount", "description" })
public class Transaction {
    public enum TransactionType {
        Credit, Debit;
    }

    public static Transaction readFrom(final JsonObject json) throws ParseException {

        final Transaction ret = new Transaction();
        ret.setAccountId(json.get("accountId").getAsInt());
        ret.setAmount(json.get("amount").getAsBigDecimal());
        ret.setDescription(json.get("description").getAsString());
        ret.setId(json.get("id").getAsInt());
        final String dt = json.get("date").getAsString();
        final Date date = DateTimeAdapter.dateFromString(dt);
        ret.setDate(date);
        ret.setType(TransactionType.valueOf(json.get("type").getAsString()));
        return ret;
    }

    private int id;

    private int accountId;

    private TransactionType type;

    private Date date;

    private BigDecimal amount;

    private String description;

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Transaction)) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        return id == other.id && accountId == other.accountId && type == other.type
            && Util.equals(date == null ? null : date.toString(), other.date == null ? null : other.date.toString())
            && Util.equals(amount, other.amount) && Util.equals(description, other.description);
    }

    public int getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    @JsonIgnore
    public int getIntType() {
        return type.ordinal();
    }

    @JsonGetter(value = "type")
    public TransactionType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + accountId;
        result = prime * result + (type == null ? 0 : type.hashCode());
        result = prime * result + (date == null ? 0 : date.toString().hashCode());
        result = prime * result + (amount == null ? 0 : amount.hashCode());
        result = prime * result + (description == null ? 0 : description.hashCode());
        return result;
    }

    public void setAccountId(final int accountId) {
        this.accountId = accountId;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @JsonIgnore
    public void setType(final int type) {
        this.type = TransactionType.values()[type];
    }

    @JsonSetter(value = "type")
    public void setType(final TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", accountId=" + accountId + ", type=" + type + ", date=" + date + ", amount="
            + amount + ", description=" + description + "]";
    }
}
