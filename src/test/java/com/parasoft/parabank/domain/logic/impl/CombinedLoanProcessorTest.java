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
 * @req PAR-34
 *
 */
public class CombinedLoanProcessorTest
extends AbstractLoanProcessorTest<CombinedLoanProcessor> {
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
