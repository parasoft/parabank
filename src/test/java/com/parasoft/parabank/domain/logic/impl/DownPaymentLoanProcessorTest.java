package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.util.LoanRequestFactory;

/**
 * @req PAR-35
 *
 */
public class DownPaymentLoanProcessorTest extends AbstractLoanProcessorTest<DownPaymentLoanProcessor> {
    @Override
    public void assertProcessor() {
        LoanRequest loanRequest = LoanRequestFactory.create(1000, 199, 1000);
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
