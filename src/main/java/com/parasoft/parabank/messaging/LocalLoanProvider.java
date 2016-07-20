package com.parasoft.parabank.messaging;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;

/**
 * Loan request client that does not use messaging and delegates to a loan
 * processor directly
 */
public class LocalLoanProvider implements LoanProvider {
    private LoanProvider loanProcessor;
    private String loanProviderName;

    public void setLoanProcessor(LoanProvider loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setLoanProviderName(String loanProviderName) {
        this.loanProviderName = loanProviderName;
    }

    @Override
    public LoanResponse requestLoan(LoanRequest loanRequest) {
        LoanResponse loanResponse = loanProcessor.requestLoan(loanRequest);
        loanResponse.setLoanProviderName("bob");
        return loanResponse;
    }
}
