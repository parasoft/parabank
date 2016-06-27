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
    
    @SuppressWarnings("serial")
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
    
    public void setParameters(Map<String, String> parameters) {
    	accessMode = parameters.get(AdminParameters.ACCESSMODE);
        endpoint = parameters.get(AdminParameters.ENDPOINT);
        soapEndpoint = parameters.get(AdminParameters.SOAP_ENDPOINT);
        restEndpoint = parameters.get(AdminParameters.REST_ENDPOINT);
        initialBalance = new BigDecimal(parameters.get(AdminParameters.INITIAL_BALANCE));
        minimumBalance = new BigDecimal(parameters.get(AdminParameters.MINIMUM_BALANCE));
        loanProvider = parameters.get(AdminParameters.LOAN_PROVIDER);
        loanProcessor = parameters.get(AdminParameters.LOAN_PROCESSOR);
        loanProcessorThreshold = Integer.parseInt(parameters.get(AdminParameters.LOAN_PROCESSOR_THRESHOLD));        
    }
    
    
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessmode) {
		this.accessMode = accessmode;
	}

	public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
    
    public String getSoapEndpoint() {
		return soapEndpoint;
	}

	public void setSoapEndpoint(String soap_endpoint) {
		this.soapEndpoint = soap_endpoint;
	}

	public String getRestEndpoint() {
		return restEndpoint;
	}

	public void setRestEndpoint(String rest_endpoint) {
		this.restEndpoint = rest_endpoint;
	}
	
	public BigDecimal getInitialBalance() {
        return initialBalance;
    }
    
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
    
    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }
    
    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    
    public String getLoanProvider() {
        return loanProvider;
    }
    
    public void setLoanProvider(String loanProvider) {
        this.loanProvider = loanProvider;
    }
    
    public String getLoanProcessor() {
        return loanProcessor;
    }
    
    public void setLoanProcessor(String loanProcessor) {
        this.loanProcessor = loanProcessor;
    }
    
    public Integer getLoanProcessorThreshold() {
        return loanProcessorThreshold;
    }
    
    public void setLoanProcessorThreshold(Integer loanProcessorThreshold) {
        this.loanProcessorThreshold = loanProcessorThreshold;
    }
}
