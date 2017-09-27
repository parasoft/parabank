package com.parasoft.parabank.domain.logic.impl;

import java.math.*;

import com.parasoft.parabank.domain.*;

/**
 * Calculates loan approval based on available funds and down payment
 */
public class CombinedLoanProcessor extends AbstractLoanProcessor {
    @Override
    protected double getQualifier(LoanRequest loanRequest) {
        return loanRequest.getFundsBalance().divide(
                loanRequest.getLoanBalance(), 3, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    protected String getErrorMessage() {
        return "error.insufficient.funds.and.down.payment";
    }
}
