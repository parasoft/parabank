package com.parasoft.parabank.domain.logic.impl;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.LoanProvider;
import com.parasoft.parabank.domain.util.LoanResponseBuilder;

/**
 * Provides skeleton algorithm for determining loan approval
 */
public abstract class AbstractLoanProcessor implements LoanProvider {
    private AdminManager adminManager;

    public void setAdminManager(AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Override
    public final LoanResponse requestLoan(LoanRequest loanRequest) {
        LoanResponseBuilder builder = new LoanResponseBuilder()
                .accountId(0)
                .approved(true);

        if (loanRequest.getDownPayment().compareTo(loanRequest.getAvailableFunds()) > 0) {
            builder.approved(false);
            builder.message("error.insufficient.funds.for.down.payment");
            return builder.build();
        }

        if (getQualifier(loanRequest) < getThreshold()) {
            builder.approved(false);
            builder.message(getErrorMessage());
        }

        return builder.build();
    }

    protected abstract double getQualifier(LoanRequest loanRequest);

    protected abstract String getErrorMessage();

    private double getThreshold() {
        String threshold = adminManager.getParameter("loanProcessorThreshold");
        return Integer.parseInt(threshold) / 100.0;
    }
}
