package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import javax.annotation.Resource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Commit;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

/**
 * @req PAR-12
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcAdminDaoTest extends AbstractAdminOperationsTest {
    private static final String TEST_PARAMETER = "loanProcessorThreshold";

    private static final String EXPECTED_VALUE = "20";

    @Resource(name = "adminDao")
    private AdminDao adminDao;

    @Override
    public void setAdminDao(final AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Test
    public void test005CleanDB() throws Exception {
        assertDBClean(() -> adminDao.cleanDB());
    }

    @Test
    @Commit
    public void test010InitalizeDB() throws Exception {
        assertDBInitialized(() -> adminDao.initializeDB());
    }

    @Test
    public void test020SetParameter() {
        final String newValue = "30";

        assertEquals(EXPECTED_VALUE, adminDao.getParameter(TEST_PARAMETER));
        adminDao.setParameter("loanProcessorThreshold", newValue);
        assertEquals(newValue, adminDao.getParameter(TEST_PARAMETER));
    }

    @Test
    public void test030GetParameters() {
        final Map<String, String> parameters = adminDao.getParameters();
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
    }

    @Test
    public void test040GetParameter() {
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
    public void test099CleanDB() throws Exception {
        assertDBClean(() -> adminDao.cleanDB());
    }
}
