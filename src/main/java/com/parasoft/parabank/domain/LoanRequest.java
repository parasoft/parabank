package com.parasoft.parabank.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.parasoft.parabank.util.DateTimeAdapter;
import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a loan application
 */
@XmlRootElement(name="loanRequest")
@XmlType(propOrder={"requestDate", "customerId", "availableFunds", "loanAmount", "downPayment"})
public class LoanRequest {
    private Date requestDate;
    private int customerId;
    private BigDecimal availableFunds;
    private BigDecimal downPayment;
    private BigDecimal loanAmount;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @XmlElement(required=true)
    public BigDecimal getAvailableFunds() {
        return availableFunds;
    }

    public void setAvailableFunds(BigDecimal availableFunds) {
        this.availableFunds = availableFunds;
    }

    @XmlElement(required=true)
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    @XmlElement(required=true)
    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getFundsBalance() {
        return availableFunds.subtract(downPayment);
    }

    public BigDecimal getLoanBalance() {
        return loanAmount.subtract(downPayment);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (requestDate == null ? 0 : requestDate.hashCode());
        result = prime * result + customerId;
        result = prime * result + (availableFunds == null ? 0 : availableFunds.hashCode());
        result = prime * result + (downPayment == null ? 0 : downPayment.hashCode());
        result = prime * result + (loanAmount == null ? 0 : loanAmount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LoanRequest)) {
            return false;
        }
        LoanRequest other = (LoanRequest)obj;
        return Util.equals(requestDate, other.requestDate) &&
            customerId == other.customerId &&
            Util.equals(availableFunds, other.availableFunds) &&
            Util.equals(downPayment, other.downPayment) &&
            Util.equals(loanAmount, other.loanAmount);
    }

    @Override
    public String toString() {
        return "LoanRequest [requestDate=" + requestDate + ", customerId="
                + customerId + ", availableFunds=" + availableFunds
                + ", downPayment=" + downPayment + ", loanAmount=" + loanAmount
                + "]";
    }
}
