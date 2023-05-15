package com.parasoft.parabank.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import com.parasoft.parabank.messaging.MockJmsListeningContainer;

public abstract class AbstractAdminOperationsTest extends AbstractParaBankDataSourceTest {
    protected interface DBCleaner {
        void cleanDB() throws Exception;
    }

    protected interface DBInitializer {
        void initializeDB() throws Exception;
    }

    protected interface JmsShutdownManager {
        void shutdownJmsListener();
    }

    protected interface JmsStartupManager {
        void startupJmsListener();
    }

    @Resource(name = "mockJmsListener")
    private MockJmsListeningContainer jmsListener;

    protected void assertDBClean(final DBCleaner dbCleaner) throws Exception {
        dbCleaner.cleanDB();
        final List<IntQuery> tests = new ArrayList<>();
        // TODO move this mess to Spring configuration
        tests.addAll(Arrays.asList(new IntQuery[] { new IntQuery(1, "SELECT COUNT(id) FROM Customer"), //$NON-NLS-1$
            new IntQuery(1, "SELECT COUNT(id) FROM Account"), //$NON-NLS-1$
            new IntQuery(0, "SELECT COUNT(id) FROM Transaction") })); //$NON-NLS-1$

        for (final IntQuery intQuery : tests) {
            intQuery.validate(getJdbcTemplate());
        }
        // assertEquals(1, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Customer"));
        // assertEquals(1, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Account"));
        // assertEquals(0, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Transaction"));
    }

    protected void assertDBInitialized(final DBInitializer dbInitializer) throws Exception {
        dbInitializer.initializeDB();
        final List<IntQuery> tests = new ArrayList<>();

        tests.addAll(Arrays.asList(new IntQuery[] { new IntQuery(2, "SELECT COUNT(id) FROM Customer"), //$NON-NLS-1$
            new IntQuery(12, "SELECT COUNT(id) FROM Account"), //$NON-NLS-1$
            new IntQuery(21, "SELECT COUNT(id) FROM Transaction"), //$NON-NLS-1$
            new IntQuery(9, "SELECT COUNT(*) FROM Parameter") })); //$NON-NLS-1$
        for (final IntQuery intQuery : tests) {
            intQuery.validate(getJdbcTemplate());
        }
        // assertEquals(2, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Customer"));
        // assertEquals(12, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Account"));
        // assertEquals(21, getJdbcTemplate().queryForInt("SELECT COUNT(id) FROM
        // Transaction"));
    }

    protected void assertJmsShutdown(final JmsShutdownManager jmsShutdownManager) {
        jmsListener.setListenerRunning(true);
        jmsListener.setListenerInitialized(true);
        jmsShutdownManager.shutdownJmsListener();
        assertFalse(jmsListener.isListenerRunning());
        assertFalse(jmsListener.isListenerInitialized());
    }

    protected void assertJmsStartup(final JmsStartupManager jmsStartupManager) {
        jmsListener.setListenerRunning(false);
        jmsListener.setListenerInitialized(false);
        jmsStartupManager.startupJmsListener();
        assertTrue(jmsListener.isListenerRunning());
        assertTrue(jmsListener.isListenerInitialized());
    }

    public MockJmsListeningContainer getJmsListener() {
        return jmsListener;
    }

    public void setJmsListener(final MockJmsListeningContainer jmsListener) {
        this.jmsListener = jmsListener;
    }
}
