package com.parasoft.parabank.service;

import com.parasoft.parabank.domain.logic.AdminManager;

public interface AdminManagerAware {

    void setAdminManager(AdminManager adminManager);

    /**
     * <DL><DT>Description:</DT><DD>
     * Getter for the adminManager property
     * </DD>
     * <DT>Date:</DT><DD>Oct 22, 2015</DD>
     * </DL>
     * @return the value of adminManager field
     */
    AdminManager getAdminManager();

}
