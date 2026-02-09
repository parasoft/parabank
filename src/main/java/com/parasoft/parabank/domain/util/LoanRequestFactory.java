package com.parasoft.parabank.domain.util;

import java.math.BigDecimal;

import com.parasoft.parabank.domain.LoanRequest;

public class LoanRequestFactory
{
    public static LoanRequest create(double availableFunds, double downPayment, double loanAmount)
    {
        LoanRequest request = new LoanRequest();
        request.setAvailableFunds(new BigDecimal(availableFunds));
        request.setDownPayment(new BigDecimal(downPayment));
        request.setLoanAmount(new BigDecimal(loanAmount));
        return request;
    }
}
