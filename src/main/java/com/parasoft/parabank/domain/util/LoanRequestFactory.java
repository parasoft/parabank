/*
* (C) Copyright ParaSoft Corporation 2018. All rights reserved.
* THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ParaSoft
* The copyright notice above does not evidence any
* actual or intended publication of such source code.
*/


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
