package com.parasoft.bookstore.jdbc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.dao.jdbc.JdbcAdminDao;

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
