package com.parasoft.parabank.service;

/**
 * <DL><DT>Description:</DT><DD>
 * TODO add  description
 * </DD>
 * <DT>Date:</DT><DD>Jun 6, 2016</DD>
 * </DL>
 *
 * @author dev - Nick Rapoport
 *
 */
public interface LoanProviderNameAware {

    /**
     * <DL><DT>Description:</DT><DD>
     * TODO add setLoanProviderName description
     * </DD>
     * <DT>Date:</DT><DD>Jun 6, 2016</DD>
     * </DL>
     * @param loanProviderName
     */
    void setLoanProviderName(String loanProviderName);

    /**
     * <DL><DT>Description:</DT><DD>
     * Getter for the loanProviderName property
     * </DD>
     * <DT>Date:</DT><DD>Jun 6, 2016</DD>
     * </DL>
     * @return the value of loanProviderName field
     */
    String getLoanProviderName();

}
