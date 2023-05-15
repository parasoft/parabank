package com.parasoft.parabank.test.util;

import static org.junit.Assert.assertEquals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO validation object for DB counts</DD>
 * <DT>Date:</DT>
 * <DD>Oct 7, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public class IntQuery {
    Logger log = LoggerFactory.getLogger(IntQuery.class);

    private final int expected;

    private final String query;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>IntQuery Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aExpected
     * @param aQuery
     * @param aAbstractAdminOperationsTest
     *            TODO
     */
    public IntQuery(final int aExpected, final String aQuery) {
        expected = aExpected;
        query = aQuery;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add validate description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aTemplate
     */
    public void validate(final JdbcTemplate aTemplate) {
        final Number number = aTemplate.queryForObject(query, Integer.class);
        final int result = number != null ? number.intValue() : 0;
        log.debug("Testing: {}, expect {}", query, result);
        assertEquals(expected, result);
    }
}
