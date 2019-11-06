package com.parasoft.parabank.service;

import com.parasoft.parabank.domain.logic.BankManager;

public interface BankManagerAware {

    void setBankManager(BankManager bankManager);

    /**
     * <DL><DT>Description:</DT><DD>
     * Getter for the bankManager property
     * </DD>
     * <DT>Date:</DT><DD>Oct 22, 2015</DD>
     * </DL>
     * @return the value of bankManager field
     */
    BankManager getBankManager();

}
