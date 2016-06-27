package com.parasoft.parabank.service;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.test.annotation.Commit;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

public class ParaBankAdminServiceImplTest extends AbstractAdminOperationsTest {
    @Resource(name = "paraBankService")
    private ParaBankService paraBankService;

    public void setParaBankService(final ParaBankService paraBankService) {
        this.paraBankService = paraBankService;
    }

    @Test
    public void testCleanDB() throws Exception {
        assertDBClean(new DBCleaner() {
            @Override
            public void cleanDB() throws Exception {
                paraBankService.cleanDB();
            }
        });
    }

    @Test
    @Commit
    public void testInitializeDB() throws Exception {
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() throws Exception {
                paraBankService.initializeDB();
            }
        });
    }

    @Test
    public void testSetParameter() {
        final String paramName = "loanProvider";
        final String paramValue = "test";
        final String SQL = "SELECT value FROM Parameter WHERE name = ?";

        assertEquals("ws", getJdbcTemplate().queryForObject(SQL, String.class, paramName));
        paraBankService.setParameter(paramName, paramValue);
        assertEquals(paramValue, getJdbcTemplate().queryForObject(SQL, String.class, paramName));
    }

    @Test
    public void testShutdownJmsListener() {
        final AdminManager am = ((AdminManagerAware) paraBankService).getAdminManager();
        final AbstractJmsListeningContainer jl = am.getJmsListener();
        am.setJmsListener(getJmsListener());
        assertJmsShutdown(new JmsShutdownManager() {
            @Override
            public void shutdownJmsListener() {
                paraBankService.shutdownJmsListener();
            }
        });
        am.setJmsListener(jl);
    }

    @Test
    public void testStartupJmsListener() {
        final AdminManager am = ((AdminManagerAware) paraBankService).getAdminManager();
        final AbstractJmsListeningContainer jl = am.getJmsListener();
        am.setJmsListener(getJmsListener());
        assertJmsStartup(new JmsStartupManager() {
            @Override
            public void startupJmsListener() {
                paraBankService.startupJmsListener();
            }
        });
        am.setJmsListener(jl);
    }
}
