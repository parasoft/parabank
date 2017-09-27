package com.parasoft.parabank.domain.logic.impl;

import java.math.*;

import com.parasoft.parabank.domain.*;

/**
 * Calculates loan approval based on down payment
 */
public class DownPaymentLoanProcessor extends AbstractLoanProcessor {
    @Override
    protected double getQualifier(LoanRequest loanRequest) {
        return loanRequest.getDownPayment().divide(
                loanRequest.getLoanAmount(), 3, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    protected String getErrorMessage() {
        return "error.insufficient.down.payment";
    }
}
