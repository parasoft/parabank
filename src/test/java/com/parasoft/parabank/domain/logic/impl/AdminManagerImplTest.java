package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

public class AdminManagerImplTest extends AbstractAdminOperationsTest {
    private static final String TEST_PARAMETER = "loanProcessorThreshold";

    private static final String EXPECTED_VALUE = "20";

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    @Transactional
    @Commit
    public void testCleanDB() throws Exception {
        assertDBClean(new DBCleaner() {
            @Override
            public void cleanDB() throws Exception {
                adminManager.cleanDB();
            }
        });
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() throws Exception {
                adminManager.initializeDB();
            }
        });
    }

    @Test
    public void testGetParameter() {
        assertEquals(EXPECTED_VALUE, adminManager.getParameter(TEST_PARAMETER));
        assertNull(adminManager.getParameter("unknown"));
    }

    @Test
    public void testGetParameters() {
        final Map<String, String> parameters = adminManager.getParameters();
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
    }

    @Test
    @Transactional
    @Commit
    public void testInitializeDB() throws Exception {
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() throws Exception {
                adminManager.initializeDB();
            }
        });
    }

    @Test
    public void testSetParameter() {
        final String newValue = "30";

        assertEquals(EXPECTED_VALUE, adminManager.getParameter(TEST_PARAMETER));
        adminManager.setParameter("loanProcessorThreshold", newValue);
        assertEquals(newValue, adminManager.getParameter(TEST_PARAMETER));
    }

    @Test
    public void testShutdownJmsListener() {
        final AbstractJmsListeningContainer jmsOrig = adminManager.getJmsListener();
        adminManager.setJmsListener(getJmsListener());
        assertJmsShutdown(new JmsShutdownManager() {
            @Override
            public void shutdownJmsListener() {
                adminManager.shutdownJmsListener();

            }
        });
        adminManager.setJmsListener(jmsOrig);
    }

    @Test
    public void testStartupJmsListener() {
        final AbstractJmsListeningContainer jmsOrig = adminManager.getJmsListener();
        adminManager.setJmsListener(getJmsListener());
        assertJmsStartup(new JmsStartupManager() {
            @Override
            public void startupJmsListener() {
                adminManager.startupJmsListener();
            }
        });
        adminManager.setJmsListener(jmsOrig);
    }
}
