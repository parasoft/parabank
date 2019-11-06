package com.parasoft.parabank.domain.logic;

import java.util.Map;

import org.springframework.jms.listener.AbstractJmsListeningContainer;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * Interface for bank system management
 */
public interface AdminManager {

    /**
     * Reset the data source and populate it with a subset of sample data
     *
     * Run this before performing a demo
     */
    void cleanDB();

    AbstractJmsListeningContainer getJmsListener();

    /**
     * Gets the value of a given configuration parameter
     *
     * @param name
     *            the name of the parameter
     * @return the value of the parameter
     */
    String getParameter(String name);

    /**
     * Gets all configurable application parameters
     *
     * @return map of application parameters
     */
    Map<String, String> getParameters();

    /**
     * Initialize the data source with the full set of sample data
     *
     * Run this first after installing ParaBank
     */
    void initializeDB();

    /**
     * Check if JMS message listener is running
     *
     * @return true if JMS message listener is running
     */
    boolean isJmsListenerRunning();

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Populate AdminForm with the current entries</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @param form
     *            the form to populate
     * @return populated form
     */
    AdminForm populateAdminForm(AdminForm form);

    void setAdminDao(AdminDao adminDao);

    void setJmsListener(AbstractJmsListeningContainer jmsListener);

    /**
     * Sets the value of a given configuration parameter
     *
     * @param name
     *            the name of the parameter
     * @param value
     *            the value to set
     */
    void setParameter(String name, String value);

    /**
     * Disable JMS message listener
     */
    void shutdownJmsListener();

    /**
     * Enable JMS message listener
     */
    void startupJmsListener();
}
