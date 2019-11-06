package com.parasoft.parabank.web.form;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.parasoft.parabank.domain.logic.AdminParameters;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-3
 * @req PAR-5
 */
public class AdminFormTest extends AbstractParaBankTest {
    private static final String ENDPOINT = "http://parabank";

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1111.11");

    private static final BigDecimal MINIMUM_BALANCE = new BigDecimal("2222.22");

    private static final String LOAN_PROVIDER = "Loan Provider";

    private static final String LOAN_PROCESSOR = "Loan Processor";

    private static final Integer LOAN_PROCESSOR_THRESHOLD = 3333;

    private static final String ACCESS_MODE = "jdbc";

    private static final String SOAP_ENDPOINT = "http://parabank/soap";

    private static final String REST_ENDPOINT = "http://parabank/rest";

    private AdminForm adminForm;

    private Map<String, String> parameters;

    @Override
    public void setUp() throws Exception {
        adminForm = new AdminForm();

        parameters = new HashMap<>();
        parameters.put(AdminParameters.ENDPOINT, ENDPOINT);
        parameters.put(AdminParameters.INITIAL_BALANCE, INITIAL_BALANCE.toString());
        parameters.put(AdminParameters.MINIMUM_BALANCE, MINIMUM_BALANCE.toString());
        parameters.put(AdminParameters.LOAN_PROVIDER, LOAN_PROVIDER);
        parameters.put(AdminParameters.LOAN_PROCESSOR, LOAN_PROCESSOR);
        parameters.put(AdminParameters.LOAN_PROCESSOR_THRESHOLD, LOAN_PROCESSOR_THRESHOLD.toString());
        parameters.put(AdminParameters.ACCESSMODE, ACCESS_MODE);
        parameters.put(AdminParameters.REST_ENDPOINT, REST_ENDPOINT);
        parameters.put(AdminParameters.SOAP_ENDPOINT, SOAP_ENDPOINT);
    }

    @Test
    public void testGetParameters() {
        adminForm.setEndpoint(ENDPOINT);
        adminForm.setInitialBalance(INITIAL_BALANCE);
        adminForm.setMinimumBalance(MINIMUM_BALANCE);
        adminForm.setLoanProvider(LOAN_PROVIDER);
        adminForm.setLoanProcessor(LOAN_PROCESSOR);
        adminForm.setLoanProcessorThreshold(LOAN_PROCESSOR_THRESHOLD);
        adminForm.setAccessMode(ACCESS_MODE);
        adminForm.setRestEndpoint(REST_ENDPOINT);
        adminForm.setSoapEndpoint(SOAP_ENDPOINT);

        final Map<String, String> adminFormParameters = adminForm.getParameters();
        assertEquals(parameters, adminFormParameters);
    }

    @Test
    public void testSetParameters() {
        adminForm.setParameters(parameters);

        assertEquals(ENDPOINT, adminForm.getEndpoint());
        assertEquals(INITIAL_BALANCE, adminForm.getInitialBalance());
        assertEquals(MINIMUM_BALANCE, adminForm.getMinimumBalance());
        assertEquals(LOAN_PROVIDER, adminForm.getLoanProvider());
        assertEquals(LOAN_PROCESSOR, adminForm.getLoanProcessor());
        assertEquals(LOAN_PROCESSOR_THRESHOLD, adminForm.getLoanProcessorThreshold());
    }
}
