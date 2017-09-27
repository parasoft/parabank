package com.parasoft.parabank.domain.logic.impl;

import static org.junit.Assert.*;

import java.util.*;

import javax.annotation.*;

import org.junit.*;
import org.junit.runners.*;
import org.springframework.jms.listener.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;

import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-31
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminManagerImplTest extends AbstractAdminOperationsTest {
    private static final String TEST_PARAMETER = "loanProcessorThreshold";

    private static final String EXPECTED_VALUE = "20";

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    public void test010GetParameters() {
        final Map<String, String> parameters = adminManager.getParameters();
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
    }

    @Test
    @Transactional
    @Commit
    public void test010InitializeDB() throws Exception {
        assertDBInitialized(() -> adminManager.initializeDB());
    }

    @Test
    public void test020GetParameter() {
        assertEquals(EXPECTED_VALUE, adminManager.getParameter(TEST_PARAMETER));
        assertNull(adminManager.getParameter("unknown"));
    }

    @Test
    public void test030SetParameter() {
        final String newValue = "30";

        assertEquals(EXPECTED_VALUE, adminManager.getParameter(TEST_PARAMETER));
        adminManager.setParameter("loanProcessorThreshold", newValue);
        assertEquals(newValue, adminManager.getParameter(TEST_PARAMETER));
    }

    @Test
    public void test040StartupJmsListener() {
        final AbstractJmsListeningContainer jmsOrig = adminManager.getJmsListener();
        adminManager.setJmsListener(getJmsListener());
        assertJmsStartup(() -> adminManager.startupJmsListener());
        adminManager.setJmsListener(jmsOrig);
    }

    @Test
    public void test050ShutdownJmsListener() {
        final AbstractJmsListeningContainer jmsOrig = adminManager.getJmsListener();
        adminManager.setJmsListener(getJmsListener());
        assertJmsShutdown(() -> adminManager.shutdownJmsListener());
        adminManager.setJmsListener(jmsOrig);
    }

    @Test
    @Transactional
    @Commit
    public void test099CleanDB() throws Exception {
        //       assertDBInitialized(() -> adminManager.initializeDB());
        assertDBClean(() -> adminManager.cleanDB());
    }
}
