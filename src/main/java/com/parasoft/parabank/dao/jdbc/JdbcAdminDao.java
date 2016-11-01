package com.parasoft.parabank.dao.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.dao.internal.DynamicDataInserter;

/*
 * JDBC implementation of AdminDao
 */
public class JdbcAdminDao extends JdbcDaoSupport implements AdminDao {
    private static final Logger log = LoggerFactory.getLogger(JdbcAdminDao.class);

    private static final String SQL_PACKAGE = "com/parasoft/parabank/dao/jdbc/sql/";

    private static final Resource CREATE_RESOURCE = new ClassPathResource(SQL_PACKAGE + "create.sql");

    private static final Resource INSERT_RESOURCE = new ClassPathResource(SQL_PACKAGE + "insert.sql");

    private static final Resource RESET_RESOURCE = new ClassPathResource(SQL_PACKAGE + "reset.sql");

    private List<DynamicDataInserter> inserters;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#cleanDB()
     */
    @Override
    public void cleanDB() {
        ResultSet rs = null;
        try {
            log.info("Validating parabank database has been initialized...");
            final DatabaseMetaData md = getConnection().getMetaData();
            rs = md.getTables("PUBLIC", "PUBLIC", "STOCK", null);
            if (rs.last()) {
                rs.close();
                performReset();
            } else {
                initializeDB();
                performReset();
            }
        } catch (final CannotGetJdbcConnectionException ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , ex);
        } catch (final SQLException ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final SQLException ex) {
                    //don't care;
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(final String name) {
        final String SQL = "SELECT value FROM Parameter WHERE name = ?";

        return getJdbcTemplate().queryForObject(SQL, String.class, name);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#getParameters()
     */
    @Override
    public Map<String, String> getParameters() {
        final String SQL = "SELECT name, value FROM Parameter";

        return getJdbcTemplate().query(SQL, (ResultSetExtractor<Map<String, String>>) rs -> {
            final Map<String, String> parameters = new HashMap<>();
            while (rs.next()) {
                parameters.put(rs.getString("name"), rs.getString("value"));
            }
            return parameters;
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#initializeDB()
     */
    @Override
    public void initializeDB() {

        log.info("Initializing parabank database...");
        ScriptUtils.executeSqlScript(getConnection(), CREATE_RESOURCE);
        log.info("Populating parabank database...");
        ScriptUtils.executeSqlScript(getConnection(), INSERT_RESOURCE);

        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), CREATE_RESOURCE,
        // false);
        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), INSERT_RESOURCE,
        // false);
        log.info("Running Dynamic Data inserters...");
        for (final DynamicDataInserter inserter : inserters) {
            inserter.insertData();
        }

        log.info("Database initialized & populated");

    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add performReset description</DD>
     * <DT>Date:</DT>
     * <DD>Nov 1, 2016</DD>
     * </DL>
     */
    private void performReset() {
        log.info("Resetting parabank database...");
        ScriptUtils.executeSqlScript(getConnection(), RESET_RESOURCE);
        // JdbcTestUtils.executeSqlScript(getJdbcTemplate(), resource, true);
        log.info("Database parabank reset");
    }

    public void setInserters(final List<DynamicDataInserter> inserters) {
        this.inserters = inserters;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.AdminDao#setParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setParameter(final String name, final String value) {
        final String SQL = "UPDATE Parameter SET value = ? WHERE name = ?";
        final String INSERT_SQL = "INSERT INTO Parameter (name,value) VALUES(?,?)";

        int rows = getJdbcTemplate().update(SQL, value, name);
        if (rows < 1) {
            log.warn("Parameter {} does not exist adding ...", name);
            rows = getJdbcTemplate().update(INSERT_SQL, name, value);
        }
    }
}
