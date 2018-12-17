package com.parasoft.parabank.domain.util;

import java.util.Date;

import com.parasoft.parabank.domain.LoanResponse;

public class LoanResponseBuilder {
    private Date responseDate;
    private String loanProviderName;
    private boolean approved;
    private String message;
    private Integer accountId;

    public LoanResponseBuilder()
    {
        responseDate = new Date();
    }

    public LoanResponseBuilder date(Date date) {
        responseDate = date;
        return this;
    }

    public LoanResponseBuilder providerName(String name) {
        loanProviderName = name;
        return this;
    }

    public LoanResponseBuilder message(String message) {
        this.message = message;
        return this;
    }

    public LoanResponseBuilder accountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    public LoanResponseBuilder approved(boolean approved) {
        this.approved = approved;
        return this;
    }

    public LoanResponse build() {
        LoanResponse response = new LoanResponse();
        if (accountId.intValue() > 0) {
            response.setAccountId(accountId);
        }
        response.setApproved(approved);
        response.setLoanProviderName(loanProviderName);
        response.setMessage(message);
        response.setResponseDate(responseDate);
        return response;
    }
}
