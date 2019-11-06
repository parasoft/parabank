package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;

import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

/**
 * @req PAR-19
 *
 */
public class JdbcSequenceDaoTest extends AbstractParaBankDataSourceTest {
    @Resource(name = "sequenceDao")
    private JdbcSequenceDao sequenceDao;

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    @Test
    public void testGetNextId() {
        for (int i = 0; i < 10; i++) {
            assertEquals(12434 + i * JdbcSequenceDao.OFFSET, sequenceDao.getNextId("Customer"));
            assertEquals(13566 + i * JdbcSequenceDao.OFFSET, sequenceDao.getNextId("Account"));
            assertEquals(14476 + i * JdbcSequenceDao.OFFSET, sequenceDao.getNextId("Transaction"));
        }

        try {
            sequenceDao.getNextId(null);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }

        try {
            sequenceDao.getNextId("");
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }
}
