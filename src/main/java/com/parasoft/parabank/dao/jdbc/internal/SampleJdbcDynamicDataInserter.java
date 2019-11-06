package com.parasoft.parabank.dao.jdbc.internal;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.parasoft.parabank.dao.internal.DynamicDataInserter;

public class SampleJdbcDynamicDataInserter extends JdbcDaoSupport implements DynamicDataInserter {
    /** {@inheritDoc} */
    @Override
    public int getDataCount() {
        // final int count = getJdbcTemplate().queryForObject(
        // "SELECT count() FROM Foo", Integer.class);
        // return count;
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public void insertData() {
        // final String SQL = "INSERT INTO Foo (bar, baz) VALUES (:bar, :baz)";

        // getJdbcTemplate().update(SQL, "bar", "baz");
    }
}
