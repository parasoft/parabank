package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.*;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Commit;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

public class JdbcAdminDaoTest extends AbstractAdminOperationsTest {
    private static final String TEST_PARAMETER = "loanProcessorThreshold";

    private static final String EXPECTED_VALUE = "20";

    @Resource(name = "adminDao")
    private AdminDao adminDao;

    public void setAdminDao(final AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Test
    public void testCleanDB() throws Exception {
        assertDBClean(new DBCleaner() {
            @Override
            public void cleanDB() {
                adminDao.cleanDB();
            }
        });
    }

    @Test
    public void testGetParameter() {
        assertEquals(EXPECTED_VALUE, adminDao.getParameter(TEST_PARAMETER));

        try {
            adminDao.getParameter(null);
            adminDao.getParameter("");
            adminDao.getParameter("unknown");
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetParameters() {
        final Map<String, String> parameters = adminDao.getParameters();
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
    }

    @Test
    @Commit
    public void testInitalizeDB() throws Exception {
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() {
                adminDao.initializeDB();
            }
        });
    }

    @Test
    public void testSetParameter() {
        final String newValue = "30";

        assertEquals(EXPECTED_VALUE, adminDao.getParameter(TEST_PARAMETER));
        adminDao.setParameter("loanProcessorThreshold", newValue);
        assertEquals(newValue, adminDao.getParameter(TEST_PARAMETER));
    }
}
