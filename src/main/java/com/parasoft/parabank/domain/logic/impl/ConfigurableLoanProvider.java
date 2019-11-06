package com.parasoft.parabank.domain.logic.impl;

import java.util.Map;
import java.util.Set;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.LoanProvider;
import com.parasoft.parabank.service.AdminManagerAware;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>Aggregate LoanProvider than can change behavior based on a parameter
 * value</DD>
 * <DT>Date:</DT>
 * <DD>Jun 6, 2016</DD>
 * </DL>
 *
 * @author dev - Nick Rapoport
 *
 */
public class ConfigurableLoanProvider implements LoanProvider, AdminManagerAware, LoanProviderMapAware {
    private AdminManager adminManager;

    private Map<String, LoanProvider> loanProviders;

    private String parameter;

    /** {@inheritDoc} */
    @Override
    public AdminManager getAdminManager() {
        return adminManager;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the loanProvider property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @return the value of loanProvider field
     */
    private LoanProvider getLoanProvider() {
        final String type = adminManager.getParameter(getParameter());
        return getLoanProviders().get(type);
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, LoanProvider> getLoanProviders() {
        return loanProviders;
    }

    /** {@inheritDoc} */
    @Override
    public String getParameter() {
        return parameter;
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> getProviderNames() {
        return getLoanProviders().keySet();
    }

    /** {@inheritDoc} */
    @Override
    public LoanResponse requestLoan(final LoanRequest loanRequest) {
        return getLoanProvider().requestLoan(loanRequest);
    }

    /** {@inheritDoc} */
    @Override
    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    public void setLoanProviders(final Map<String, LoanProvider> loanProviders) {
        this.loanProviders = loanProviders;
    }

    /** {@inheritDoc} */
    @Override
    public void setParameter(final String parameter) {
        this.parameter = parameter;
    }
}
