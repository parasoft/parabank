package com.parasoft.parabank.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.impl.ConfigurableLoanProvider;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

/**
 * @req PAR-39
 *
 */
public class LocalLoanProviderTest extends AbstractParaBankDataSourceTest {
    private static final String TEST_PROVIDER = "Test Provider";

    private LocalLoanProvider localLoanProvider;

    @Resource(name = "loanProvider")
    private ConfigurableLoanProvider loanProvider;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        localLoanProvider = new LocalLoanProvider();
        localLoanProvider.setLoanProcessor(loanProvider);
        localLoanProvider.setLoanProviderName(TEST_PROVIDER);
    }

    public void setLoanProvider(final ConfigurableLoanProvider loanProvider) {
        this.loanProvider = loanProvider;
    }

    @Test
    public void testRequestLoan() {
        final LoanRequest loanRequest = new LoanRequest();
        loanRequest.setAvailableFunds(new BigDecimal("1000.00"));
        loanRequest.setDownPayment(new BigDecimal("100.00"));
        loanRequest.setLoanAmount(new BigDecimal("5000.00"));

        final LoanResponse response = localLoanProvider.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals(TEST_PROVIDER, response.getLoanProviderName());
    }
}
