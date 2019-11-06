package com.parasoft.parabank.service;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;

/**
 * Java interface for loan web service
 */
@WebService(targetNamespace=LoanProcessorService.TNS)
public interface LoanProcessorService {
    String TNS = "http://service.parabank.parasoft.com/";

    /**
     * Request a loan
     *
     * @param customerId the customer id to lookup
     * @return the customer
     * @throws ParaBankServiceException
     */
    @WebResult(name="loanResponse", targetNamespace=TNS)
    LoanResponse requestLoan(
        @WebParam(name="loanRequest", targetNamespace=TNS)
        LoanRequest loanRequest
    ) throws ParaBankServiceException;
}
