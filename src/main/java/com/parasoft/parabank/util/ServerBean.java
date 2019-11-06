package com.parasoft.parabank.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerConfiguration;
import org.hsqldb.server.ServerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Utility bean for embedding HSQLDB into the ParaBank application
 */
public class ServerBean implements InitializingBean, DisposableBean, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ServerBean.class);

    private Properties serverProperties;

    private Server server;

    private DataSource dataSource;

    ApplicationContext context;

    private String databasePath = null;

    // System.getProperty("catalina.base") + "/webapps/parabank/db/db";
    private String databasePath2 = null;
    // System.getProperty("catalina.base") + "/webapps/parabank/db/db2";

    @Override
    public void afterPropertiesSet() throws Exception {
        final HsqlProperties configProps = new HsqlProperties(serverProperties);

        ServerConfiguration.translateDefaultDatabaseProperty(configProps);

        server = new Server();
        server.setRestartOnShutdown(false);
        server.setNoSystemExit(true);
        server.setProperties(configProps);
        if (databasePath == null || databasePath2 == null) {
            final String currPath = Util.getCurrentPath(getApplicationContext());
            if (databasePath == null) {
                databasePath = String.format(Constants.DB_PATH_FMT, currPath, "db");
            }

            if (databasePath2 == null) {
                databasePath2 = String.format(Constants.DB_PATH_FMT, currPath, "db2");
            }
        }
        server.setDatabasePath(0, databasePath);
        server.setDatabasePath(1, databasePath2);

        log.info("HSQL Server Startup sequence initiated");

        server.start();

        final String portMsg = "port " + server.getPort();
        log.info("HSQL Server listening on " + portMsg);
    }

    /**
     * <DL><DT>Description:</DT><DD>
     * remove registered JDBC Drivers -- this will prevent Memory Leak messages from Tomcat
     * </DD>
     * <DT>Date:</DT><DD>Oct 16, 2016</DD>
     * </DL>
     */
    private void deregisterJDBCDrivers() {
        for (final Enumeration<Driver> drivers = DriverManager.getDrivers(); drivers.hasMoreElements();) {
            final Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (final SQLException e) {
                log.error("Exception caught while deregistering JDBC drivers", e);
            } finally {
                log.debug("De-register HSQLDB Driver {} completed", driver.getClass().getName());

            }
        }
        log.info("De-register HSQLDB Driver completed");
    }

    /** {@inheritDoc}
     * <DL><DT>Description:</DT><DD>
     * Shutdown the HSQLDB gracefully at application unload. The intent is to prevent memory and resource leaks. Also unregister all JDBC drivers
     * </DD>
     * <DT>Date:</DT><DD>Oct 16, 2016</DD>
     * </DL>
     */
    @Override
    @PreDestroy
    public void destroy() {
        final int status = shutdownHSQLDBServer();
        if (status != ServerConstants.SERVER_STATE_SHUTDOWN) {
            log.warn("HSQL Server failed to shutdown properly.");
        } else {
            log.info("HSQL Server Shutdown completed");
        }
        server = null;
        deregisterJDBCDrivers();

    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the context property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of context field
     */
    public ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the databasePath property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of databasePath field
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the databasePath2 property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of databasePath2 field
     */
    public String getDatabasePath2() {
        return databasePath2;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the dataSource property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of dataSource field
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the server property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of server field
     */
    public Server getServer() {
        return server;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the serverProperties property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @return the value of serverProperties field
     */
    public Properties getServerProperties() {
        return serverProperties;
    }

    /**
     * {@inheritDoc}
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the context property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aContext
     *            new value for the context property
     */
    @Override
    public void setApplicationContext(final ApplicationContext aContext) {
        context = aContext;
    }

    public void setDatabasePath(final String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the databasePath2 property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aDatabasePath2
     *            new value for the databasePath2 property
     */
    public void setDatabasePath2(final String aDatabasePath2) {
        databasePath2 = aDatabasePath2;
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the server property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 7, 2015</DD>
     * </DL>
     *
     * @param aServer
     *            new value for the server property
     */
    public void setServer(final Server aServer) {
        server = aServer;
    }

    public void setServerProperties(final Properties serverProperties) {
        this.serverProperties = serverProperties;
    }

    /**
     * <DL><DT>Description:</DT><DD>
     * shutdown HSQLDB Server. wait for the server  to finish shutting down (or timeout 5 seconds). return current state
     * </DD>
     * <DT>Date:</DT><DD>Oct 16, 2016</DD>
     * </DL>
     * @return the status from the server.getState() command
     */
    private int shutdownHSQLDBServer() {
        log.info("HSQL Server Shutdown sequence initiated");
        if (dataSource != null) {
            Connection con = null;
            try {
                con = dataSource.getConnection();
                con.createStatement().execute("SHUTDOWN");
            } catch (final SQLException ex) {
                log.error("HSQL Server Shutdown failed: ", ex);
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (final Exception e) {
                }
            }
        } else {
            log.warn("HSQL ServerBean needs a dataSource property set to shutdown database safely.");
        }
        server.signalCloseAllServerConnections();
        server.shutdownWithCatalogs(org.hsqldb.Database.CLOSEMODE_NORMAL);

        int status = server.stop();
        final long timeout = System.currentTimeMillis() + 5000;
        while (status != ServerConstants.SERVER_STATE_SHUTDOWN && System.currentTimeMillis() < timeout) {
            try {
                Thread.sleep(100);
                status = server.getState();
            } catch (final InterruptedException e) {
                log.error("Error while shutting down HSQL Server: ", e);
                break;
            }
        }
        return status;
    }
}
