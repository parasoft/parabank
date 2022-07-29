package com.parasoft.parabank.domain;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a stock position
 */
@XmlRootElement(name="position" )
@XmlType(propOrder={"positionId", "customerId", "name", "symbol", "shares", "purchasePrice"})
public class Position {
    private int positionId;
    private int customerId;
    private String name;
    private String symbol;
    private int shares;
    private BigDecimal purchasePrice;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int id) {
        positionId = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int id) {
        customerId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal price) {
        purchasePrice = price;
    }

    @XmlTransient
    @JsonIgnore
    public BigDecimal getCostBasis() {
        return purchasePrice.multiply(new BigDecimal(shares));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + positionId;
        result = prime * result + customerId;
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (symbol == null ? 0 : symbol.hashCode());
        result = prime * result + shares;
        result = prime * result + (purchasePrice == null ? 0 : purchasePrice.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Position)) {
            return false;
        }
        Position other = (Position) obj;
        return positionId == other.positionId &&
            customerId == other.customerId &&
            Util.equals(name, other.name) &&
            Util.equals(symbol, other.symbol) &&
            shares == other.shares &&
            Util.equals(purchasePrice, other.purchasePrice);
    }

    @Override
    public String toString() {
       return "Position [positionId=" + positionId + ", customerId=" + customerId + ", name=" + name + ", symbol="
                + symbol + ", shares=" + shares + ", purchasePrice=" + purchasePrice + "]";
    }
}
