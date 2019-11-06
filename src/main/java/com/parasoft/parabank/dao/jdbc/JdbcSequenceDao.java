package com.parasoft.parabank.dao.jdbc;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Manages sequences to automatically generate ids for new entities
 */
public class JdbcSequenceDao extends JdbcDaoSupport {
    public static final int OFFSET = 111;

    public int getCurrentId(final String name) {
        final Number number = getJdbcTemplate().queryForObject("SELECT next_id FROM Sequence WHERE name = ?",
            new Object[] { name }, Integer.class);
        final int nextId = number != null ? number.intValue() : 0;
        // int nextId = getJdbcTemplate().queryForInt("SELECT next_id FROM
        // Sequence WHERE name = ?", name);
        // getJdbcTemplate().update("UPDATE Sequence SET next_id = ? WHERE name
        // = ?", nextId + OFFSET, name);
        return nextId;
    }

    /**
     * Generate and return the next id for a given entity
     *
     * @param name
     *            the name of the database table to generate an id
     * @return a new id value to be used for a new entity object
     */
    public int getNextId(final String name) {
        final Number number = getJdbcTemplate().queryForObject("SELECT next_id FROM Sequence WHERE name = ?",
            new Object[] { name }, Integer.class);
        final int nextId = number != null ? number.intValue() : 0;
        // int nextId = getJdbcTemplate().queryForInt("SELECT next_id FROM
        // Sequence WHERE name = ?", name);
        getJdbcTemplate().update("UPDATE Sequence SET next_id = ? WHERE name = ?", nextId + OFFSET, name);
        return nextId;
    }

    public int setNextId(final String name, final int nextId) {
        getJdbcTemplate().update("UPDATE Sequence SET next_id = ? WHERE name = ?", nextId + OFFSET, name);
        final Number number = getJdbcTemplate().queryForObject("SELECT next_id FROM Sequence WHERE name = ?",
            new Object[] { name }, Integer.class);
        final int newNextId = number != null ? number.intValue() : 0;
        return newNextId;
    }

}
