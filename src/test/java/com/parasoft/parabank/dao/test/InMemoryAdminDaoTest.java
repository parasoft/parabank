package com.parasoft.parabank.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.dao.InMemoryAdminDao;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-22
 *
 */
public class InMemoryAdminDaoTest extends AbstractParaBankTest {
    private static final String NAME1 = "name1";

    private static final String VALUE1 = "value1";

    private static final String NAME2 = "name2";

    private static final String VALUE2 = "value2";

    private AdminDao adminDao;

    @Override
    public void setUp() throws Exception {
        final Map<String, String> parameters = new HashMap<>();

        parameters.put(NAME1, VALUE1);
        parameters.put(NAME2, VALUE2);

        adminDao = new InMemoryAdminDao(parameters);
    }

    @Test
    public void testGetParameter() {
        assertEquals(VALUE1, adminDao.getParameter(NAME1));
        assertEquals(VALUE2, adminDao.getParameter(NAME2));

        assertNull(adminDao.getParameter(null));
        assertNull(adminDao.getParameter(""));
        assertNull(adminDao.getParameter("unknown"));
    }

    @Test
    public void testGetParameters() {
        assertNotNull(adminDao.getParameters());
        assertEquals(2, adminDao.getParameters().size());
    }

    @Test
    public void testNullMethods() {
        adminDao.initializeDB();
        adminDao.cleanDB();
    }

    @Test
    public void testSetParameter() {
        assertEquals(VALUE1, adminDao.getParameter(NAME1));
        adminDao.setParameter(NAME1, VALUE2);
        assertEquals(VALUE2, adminDao.getParameter(NAME1));
    }
}
