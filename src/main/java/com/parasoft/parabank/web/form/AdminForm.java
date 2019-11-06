package com.parasoft.parabank.web.form;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.parasoft.parabank.domain.logic.AdminParameters;

/**
 * Backing class for admin settings form
 */
public class AdminForm {
    //private Map<String, String> parameters;
    private String endpoint;
    private String soapEndpoint;
    private String restEndpoint;
    private BigDecimal initialBalance;
    private BigDecimal minimumBalance;
    private String loanProvider;
    private String loanProcessor;
    private Integer loanProcessorThreshold;
    private String accessMode;

    public String getAccessMode() {
        return accessMode;
    }

    public String getEndpoint() {
        return endpoint;
    }



    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public String getLoanProcessor() {
        return loanProcessor;
    }

    public Integer getLoanProcessorThreshold() {
        return loanProcessorThreshold;
    }

    public String getLoanProvider() {
        return loanProvider;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public Map<String, String> getParameters() {
        return new HashMap<String, String>() {{
            put(AdminParameters.ENDPOINT, endpoint);
            put(AdminParameters.ACCESSMODE,accessMode);
            put(AdminParameters.SOAP_ENDPOINT, soapEndpoint);
            put(AdminParameters.REST_ENDPOINT, restEndpoint);
            put(AdminParameters.INITIAL_BALANCE, initialBalance.toString());
            put(AdminParameters.MINIMUM_BALANCE, minimumBalance.toString());
            put(AdminParameters.LOAN_PROVIDER, loanProvider);
            put(AdminParameters.LOAN_PROCESSOR, loanProcessor);
            put(AdminParameters.LOAN_PROCESSOR_THRESHOLD, loanProcessorThreshold.toString());
        }};
    }

    public String getRestEndpoint() {
        return restEndpoint;
    }

    public String getSoapEndpoint() {
        return soapEndpoint;
    }

    public void setAccessMode(final String accessmode) {
        accessMode = accessmode;
    }

    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public void setInitialBalance(final BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public void setLoanProcessor(final String loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setLoanProcessorThreshold(final Integer loanProcessorThreshold) {
        this.loanProcessorThreshold = loanProcessorThreshold;
    }

    public void setLoanProvider(final String loanProvider) {
        this.loanProvider = loanProvider;
    }

    public void setMinimumBalance(final BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public void setParameters(final Map<String, String> parameters) {
        accessMode = parameters.get(AdminParameters.ACCESSMODE);
        endpoint = parameters.get(AdminParameters.ENDPOINT);
        soapEndpoint = parameters.get(AdminParameters.SOAP_ENDPOINT);
        restEndpoint = parameters.get(AdminParameters.REST_ENDPOINT);
        String varString = parameters.get(AdminParameters.INITIAL_BALANCE);
        initialBalance = new BigDecimal(varString == null || varString.isEmpty() ? "515.55" : varString);
        varString = parameters.get(AdminParameters.MINIMUM_BALANCE);
        minimumBalance = new BigDecimal(varString == null || varString.isEmpty() ? "100.00" : varString);
        loanProvider = parameters.get(AdminParameters.LOAN_PROVIDER);
        loanProcessor = parameters.get(AdminParameters.LOAN_PROCESSOR);
        varString=parameters.get(AdminParameters.LOAN_PROCESSOR_THRESHOLD);
        loanProcessorThreshold = Integer.parseInt(varString == null || varString.isEmpty() ? "20" : varString);
    }

    public void setRestEndpoint(final String rest_endpoint) {
        restEndpoint = rest_endpoint;
    }

    public void setSoapEndpoint(final String soap_endpoint) {
        soapEndpoint = soap_endpoint;
    }
}
