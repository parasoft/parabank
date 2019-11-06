package com.parasoft.parabank.service;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.LoanProvider;

public class LoanProcessorServiceImpl implements LoanProcessorService, LoanProcessorAware, LoanProviderNameAware {
    private LoanProvider loanProcessor;

    private String loanProviderName;

    /** {@inheritDoc} */
    @Override
    public LoanProvider getLoanProcessor() {
        return loanProcessor;
    }

    /** {@inheritDoc} */
    @Override
    public String getLoanProviderName() {
        return loanProviderName;
    }

    /** {@inheritDoc} */
    @Override
    public LoanResponse requestLoan(final LoanRequest loanRequest) throws ParaBankServiceException {
        final LoanResponse response = getLoanProcessor().requestLoan(loanRequest);
        response.setLoanProviderName(getLoanProviderName());
        return response;
    }

    /** {@inheritDoc} */
    @Override
    public void setLoanProcessor(final LoanProvider loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    /** {@inheritDoc} */
    @Override
    public void setLoanProviderName(final String loanProviderName) {
        this.loanProviderName = loanProviderName;
    }
}
