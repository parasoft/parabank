package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.math.*;

import com.parasoft.parabank.domain.*;
/**
 * @req PAR-34
 *
 */
public class CombinedLoanProcessorTest
extends AbstractLoanProcessorTest<CombinedLoanProcessor> {
    @Override
    public void assertProcessor() {
        loanRequest.setDownPayment(BigDecimal.ZERO);
        loanRequest.setLoanAmount(new BigDecimal("4900.00"));
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
