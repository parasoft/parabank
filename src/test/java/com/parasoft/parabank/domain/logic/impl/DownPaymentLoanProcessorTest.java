package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.math.*;

import com.parasoft.parabank.domain.*;

/**
 * @req PAR-35
 *
 */
public class DownPaymentLoanProcessorTest extends AbstractLoanProcessorTest<DownPaymentLoanProcessor> {
    @Override
    public void assertProcessor() {
        loanRequest.setLoanAmount(new BigDecimal("1000.00"));
        loanRequest.setDownPayment(new BigDecimal("199.00"));
        LoanResponse response = processor.requestLoan(loanRequest);
        assertFalse(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals(processor.getErrorMessage(), response.getMessage());

        loanRequest.setDownPayment(new BigDecimal("200.00"));
        response = processor.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertNull(response.getMessage());

        loanRequest.setDownPayment(new BigDecimal("201.00"));
        response = processor.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertNull(response.getMessage());
    }
}
