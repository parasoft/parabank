package com.parasoft.parabank.service;

import com.parasoft.parabank.domain.logic.LoanProvider;

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
public interface LoanProcessorAware {

    /**
     * <DL><DT>Description:</DT><DD>
     * TODO add setLoanProcessor description
     * </DD>
     * <DT>Date:</DT><DD>Jun 6, 2016</DD>
     * </DL>
     * @param loanProcessor
     */
    void setLoanProcessor(LoanProvider loanProcessor);

    /**
     * <DL><DT>Description:</DT><DD>
     * Getter for the loanProcessor property
     * </DD>
     * <DT>Date:</DT><DD>Jun 6, 2016</DD>
     * </DL>
     * @return the value of loanProcessor field
     */
    LoanProvider getLoanProcessor();

}
