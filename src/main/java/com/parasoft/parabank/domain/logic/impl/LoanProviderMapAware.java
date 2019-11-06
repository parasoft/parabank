package com.parasoft.parabank.domain.logic.impl;

import java.util.Map;
import java.util.Set;

import com.parasoft.parabank.domain.logic.LoanProvider;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>Aggregate (Map) LoanProvider that can change behavior based on a
 * parameter value, which is used to get the specific type provider from the map
 * </DD>
 * <DT>Date:</DT>
 * <DD>Jun 6, 2016</DD>
 * </DL>
 *
 * @author dev - Nick Rapoport
 *
 */
public interface LoanProviderMapAware {

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the loanProviders property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @return the value of loanProviders field
     */
    Map<String, LoanProvider> getLoanProviders();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the parameter property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @return the value of parameter field
     */
    String getParameter();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the providerNames property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @return the value of providerNames field
     */
    Set<String> getProviderNames();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the loanProviders property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @param loanProviders
     *            the new value of loanProviders field
     */
    void setLoanProviders(Map<String, LoanProvider> loanProviders);

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the parameter property</DD>
     * <DT>Date:</DT>
     * <DD>Jun 6, 2016</DD>
     * </DL>
     *
     * @param parameter
     *            the new value of parameter field
     */
    void setParameter(String parameter);

}
