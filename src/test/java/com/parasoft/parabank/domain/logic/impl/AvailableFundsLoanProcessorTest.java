package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.math.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.util.LoanRequestFactory;

/**
 * @req PAR-32
 *
 */
public class AvailableFundsLoanProcessorTest
extends AbstractLoanProcessorTest<AvailableFundsLoanProcessor> {
    @Override
    public void assertProcessor() {
        LoanRequest loanRequest = LoanRequestFactory.create(1000, 0, 4900);
        LoanResponse response = processor.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertNull(response.getMessage());

        loanRequest.setLoanAmount(new BigDecimal("5000.00"));
        response = processor.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertNull(response.getMessage());

        loanRequest.setLoanAmount(new BigDecimal("5100.00"));
        response = processor.requestLoan(loanRequest);
        assertFalse(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals(processor.getErrorMessage(), response.getMessage());
    }
}
