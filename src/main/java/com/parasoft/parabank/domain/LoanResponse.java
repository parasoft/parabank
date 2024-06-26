package com.parasoft.parabank.domain;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.parasoft.parabank.util.DateTimeAdapter;
import com.parasoft.parabank.util.Util;


/**
 * Domain object representing a loan application response
 */
@XmlRootElement(name="loanResponse" , namespace="http://service.parabank.parasoft.com/")
@XmlType(propOrder={"responseDate", "loanProviderName", "approved", "message", "accountId"})
public class LoanResponse {
    private Date responseDate;
    private String loanProviderName;
    private boolean approved;
    private String message;
    private Integer accountId;

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @XmlSchemaType(name = "dateTime")
    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    @XmlElement(required=true)
    public String getLoanProviderName() {
        return loanProviderName;
    }

    public void setLoanProviderName(String loanProviderName) {
        this.loanProviderName = loanProviderName;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @JsonInclude(value = Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (responseDate == null ? 0 : responseDate.hashCode());
        result = prime * result + (loanProviderName == null ? 0 : loanProviderName.hashCode());
        result = prime * result + (approved ? 1231 : 1237);
        result = prime * result + (message == null ? 0 : message.hashCode());
        result = prime * result + (accountId == null ? 0 : accountId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LoanResponse)) {
            return false;
        }
        LoanResponse other = (LoanResponse)obj;
        return Util.equals(responseDate, other.responseDate) &&
            Util.equals(loanProviderName, other.loanProviderName) &&
            approved == other.approved &&
            Util.equals(message, other.message) &&
            Util.equals(accountId, other.accountId);
    }

    @Override
    public String toString() {
        return "LoanResponse [responseDate=" + responseDate
                + ", loanProviderName=" + loanProviderName + ", approved="
                + approved + ", message=" + message + ", accountId="
                + accountId + "]";
    }
}
