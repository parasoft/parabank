package com.parasoft.bookstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public abstract class DB {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DB.class);

    private static ResourceBundle bookstoreResources = ResourceBundle.getBundle("jdbcBookstore");

    // NYI make these settable by the deployment descriptor
    private static final String NL_DRIVER = getProperty("jdbc.bookstoreDriverClassName", "org.hsqldb.jdbcDriver");

    private static final String NL_HOST = getProperty("jdbc.bookstorePort", "localhost");

    private static final String NL_PORT = getProperty("jdbc.bookstorePort", "");

    private static final String NL_DBNAME = getProperty("jdbc.bookstoreDatabase", "bookstore");

    private static final String NL_USERNAME = getProperty("jdbc.bookstoreUsername", "sa");

    private static final String NL_PASSWORD = getProperty("jdbc.bookstorePassword", "");

    private static final String NL_CONNECTION_URL =
        getProperty("jdbc.bookstoreURL", "jdbc:hsqldb:hsql://" + NL_HOST + NL_PORT + "/" + NL_DBNAME);

    private static final String getProperty(final String key, final String defaultValue) {
        if (bookstoreResources.containsKey(key)) {
            return bookstoreResources.getString(key);
        }
        return defaultValue;// "jdbc:hsqldb:hsql://" + NL_HOST + NL_PORT + "/" + NL_DBNAME;// + "&characterEncoding=UTF8";
    }

    private Connection connection;

    protected DB() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        final Object o = Class.forName(NL_DRIVER).newInstance();
        log.trace("got driver: {}", o.getClass().getName());
        connect();
    }

    public void close() throws SQLException {
        connection.close();
    }

    protected void connect() throws SQLException {
        if (isClosed()) {
            connection = DriverManager.getConnection(NL_CONNECTION_URL, NL_USERNAME, NL_PASSWORD);
        }
    }

    /**
     * Don't rely on this, call close() manually.
     */
    @Override
    public void finalize() throws Throwable {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        } finally {
            super.finalize();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isClosed() throws SQLException {
        return connection == null || connection.isClosed();
    }

    /**
     * Call close() when finished.
     *
     * @param query
     *            an SQL statement
     */
    protected PreparedStatement prepareStatement(final String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    protected PreparedStatement prepareStatement(final String query, final int resultSetType,
        final int resultSetConcurrency) throws SQLException {
        return connection.prepareStatement(query, resultSetType, resultSetConcurrency);
    }
}
