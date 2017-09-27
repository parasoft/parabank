package com.parasoft.parabank.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

import java.math.*;
import java.util.*;

import org.junit.*;
import org.slf4j.*;

import com.github.tomakehurst.wiremock.verification.*;
import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.domain.logic.impl.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-41
 *
 */
public class LoanProcessorServiceImplTest extends AbstractParaBankDataSourceTest {
    private final static Logger log = LoggerFactory.getLogger(LoanProcessorServiceImplTest.class);

    @javax.annotation.Resource(name = "loanProcessorService")
    private LoanProcessorService loanProcessorService;

    private LoanRequest loanRequest;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        if (loanProcessorService instanceof LoanProcessorAware) {
            final LoanProvider lp = ((LoanProcessorAware) loanProcessorService).getLoanProcessor();
            if (lp instanceof LoanProviderMapAware) {
                ((LoanProviderMapAware) lp).setParameter("loanProcessor");
            }
        }
        loanRequest = new LoanRequest();
        loanRequest.setCustomerId(12345);
        loanRequest.setAvailableFunds(new BigDecimal("1000.00"));

    }

    public void setLoanProcessorService(final LoanProcessorService loanProcessorService) {
        this.loanProcessorService = loanProcessorService;
    }

    @Test
    public void test01RequestLoan() throws Exception {
        loanRequest.setLoanAmount(new BigDecimal("100.00"));
        loanRequest.setDownPayment(new BigDecimal("10.00"));
        LoanResponse response = loanProcessorService.requestLoan(loanRequest);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals("Test Provider", response.getLoanProviderName());

        loanRequest.setLoanAmount(new BigDecimal("10000.00"));
        response = loanProcessorService.requestLoan(loanRequest);
        assertFalse(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertEquals("error.insufficient.funds", response.getMessage());
        assertEquals("Test Provider", response.getLoanProviderName());
    }

    /**
     * Test method for
     * {@link com.parasoft.parabank.messaging.WebServiceLoanProvider#requestLoan(com.parasoft.parabank.domain.LoanRequest)}
     * .
     */
    @Test
    public void test02WebRequestLoan() throws Exception {
        if (loanProcessorService instanceof LoanProcessorAware) {
            final LoanProvider lp = ((LoanProcessorAware) loanProcessorService).getLoanProcessor();
            if (lp instanceof LoanProviderMapAware) {
                ((LoanProviderMapAware) lp).setParameter("loanProvider");
                // this will switch the loan provider to WS
            }
        }

        for (final LoanRequestTestConfig key : getResources()) {
            if (key.isLoanRequest()) {
                log.info("LoanRequest for ${} started ", key.getAmount());
                loanRequest.setLoanAmount(key.getAmountBigDecimal());
                loanRequest.setDownPayment(key.getDownPaymentBigDecimal());
                final LoanResponse response = loanProcessorService.requestLoan(loanRequest);
                key.validateResults(response, "Test Provider");
                log.info("LoanRequest for ${} validated ", key.getAmount());
            }
        }

        if (log.isTraceEnabled()) {
            final List<LoggedRequest> requests =
                findAll(postRequestedFor(urlPathMatching("/parabank/services/LoanProcessor")));
            for (final LoggedRequest loggedRequest : requests) {
                log.info("Request Body {}", loggedRequest.getBodyAsString());
            }
        }
    }
}
