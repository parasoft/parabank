package com.parasoft.parabank.domain.logic.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jms.listener.AbstractJmsListeningContainer;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.web.form.AdminForm;

/*
 * Implementation of AdminManager
 */
// @Component("adminManager")
public class AdminManagerImpl implements AdminManager {
    private static final Logger log = LoggerFactory.getLogger(AdminManagerImpl.class);

    //@Resource(name = "adminDao")
    private AdminDao adminDao;

    //@Resource(name = "jmsListener")
    private AbstractJmsListeningContainer jmsListener;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#cleanDB()
     */
    @Override
    public void cleanDB() {
        adminDao.cleanDB();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the jmsListener property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 19, 2015</DD>
     * </DL>
     *
     * @return the value of jmsListener field
     */
    @Override
    public AbstractJmsListeningContainer getJmsListener() {
        return jmsListener;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String name) {
        try {
            return adminDao.getParameter(name);
        } catch (final DataAccessException e) {
            log.error("Could not retrieve parameter with name: " + name);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#getParameters()
     */
    @Override
    public Map<String, String> getParameters() {
        return adminDao.getParameters();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#initializeDB()
     */
    @Override
    public void initializeDB() {
        adminDao.initializeDB();
    }

    @Override
    public boolean isJmsListenerRunning() {
        return jmsListener.isRunning();
    }

    @Override
    public AdminForm populateAdminForm(final AdminForm aForm) {
        aForm.setParameters(getParameters());
        log.info("form parameters = {}", aForm.getParameters());
        return aForm;
    }

    @Override
    public void setAdminDao(final AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public void setJmsListener(final AbstractJmsListeningContainer jmsListener) {
        this.jmsListener = jmsListener;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#setParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setParameter(final String name, final String value) {
        adminDao.setParameter(name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#shutdownJmsListener()
     */
    @Override
    public void shutdownJmsListener() {
        jmsListener.stop();
        jmsListener.shutdown();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.AdminManager#startupJmsListener()
     */
    @Override
    public void startupJmsListener() {
        jmsListener.start();
        jmsListener.initialize();
    }
}
