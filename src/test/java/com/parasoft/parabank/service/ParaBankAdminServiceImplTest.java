package com.parasoft.parabank.service;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.test.annotation.Commit;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

/**
 * @req PAR-41
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParaBankAdminServiceImplTest extends AbstractAdminOperationsTest {
    @Resource(name = "paraBankService")
    private ParaBankService paraBankService;

    public void setParaBankService(final ParaBankService paraBankService) {
        this.paraBankService = paraBankService;
    }

    @Test
    @Commit
    public void test010InitializeDB() throws Exception {
        assertDBInitialized(() -> paraBankService.initializeDB());
    }

    @Test
    public void test020SetParameter() {
        final String paramName = "loanProvider";
        final String paramValue = "test";
        final String SQL = "SELECT value FROM Parameter WHERE name = ?";

        assertEquals("ws", getJdbcTemplate().queryForObject(SQL, String.class, paramName));
        paraBankService.setParameter(paramName, paramValue);
        assertEquals(paramValue, getJdbcTemplate().queryForObject(SQL, String.class, paramName));
    }

    @Test
    public void test030StartupJmsListener() {
        final AdminManager am = ((AdminManagerAware) paraBankService).getAdminManager();
        final AbstractJmsListeningContainer jl = am.getJmsListener();
        am.setJmsListener(getJmsListener());
        assertJmsStartup(() -> paraBankService.startupJmsListener());
        am.setJmsListener(jl);
    }

    @Test
    public void test040ShutdownJmsListener() {
        final AdminManager am = ((AdminManagerAware) paraBankService).getAdminManager();
        final AbstractJmsListeningContainer jl = am.getJmsListener();
        am.setJmsListener(getJmsListener());
        assertJmsShutdown(() -> paraBankService.shutdownJmsListener());
        am.setJmsListener(jl);
    }

    @Test
    public void test099CleanDB() throws Exception {
        assertDBClean(() -> paraBankService.cleanDB());
    }
}
