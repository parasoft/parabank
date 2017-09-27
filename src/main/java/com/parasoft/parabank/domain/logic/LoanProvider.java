package com.parasoft.parabank.domain.logic;

import com.parasoft.parabank.domain.*;

/**
 * Interface for objects that can provide/process a loan
 */
public interface LoanProvider {

    /**
     * Submit a loan request to a loan provider
     * @param loanRequest the loan request details
     * @return response containing the loan status and any additional info
     */
    LoanResponse requestLoan(LoanRequest loanRequest);
}
