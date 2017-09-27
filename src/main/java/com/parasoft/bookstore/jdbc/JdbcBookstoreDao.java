package com.parasoft.bookstore.jdbc;

import java.util.*;

import org.slf4j.*;
import org.springframework.core.io.*;
import org.springframework.jdbc.core.support.*;
import org.springframework.jdbc.datasource.init.*;

import com.parasoft.parabank.dao.*;
import com.parasoft.parabank.dao.jdbc.*;

public class JdbcBookstoreDao extends JdbcDaoSupport implements AdminDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcAdminDao.class);

    private static final String SQL_PACKAGE = "com/parasoft/bookstore/jdbc/sql/";

    private static final Resource CREATE_RESOURCE = new ClassPathResource(SQL_PACKAGE + "bookstoreCreate.sql");

    private static final Resource INSERT_RESOURCE = new ClassPathResource(SQL_PACKAGE + "bookstoreInsert.sql");

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#cleanDB()
     */
    @Override
    public void cleanDB() {
        // TODO NYI
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String name) {
        // TODO NYI
        return "";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#getParameters()
     */
    @Override
    public Map<String, String> getParameters() {
        // TODO NYI
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#initializeDB()
     */
    @Override
    public void initializeDB() {

        log.info("Initializing database...");
        ScriptUtils.executeSqlScript(getConnection(), CREATE_RESOURCE);
        ScriptUtils.executeSqlScript(getConnection(), INSERT_RESOURCE);
        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), CREATE_RESOURCE,
        // false);
        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), INSERT_RESOURCE,
        // false);

        log.info("Database initialized");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#setParameter(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void setParameter(final String name, final String value) {
        // TODO NYI
    }
}
