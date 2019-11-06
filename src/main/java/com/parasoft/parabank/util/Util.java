package com.parasoft.parabank.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.web.UserSession;

/**
 * Utility methods for ParaBank
 */
public final class Util {
    public static final int DEFAULT_CATALINA_PORT = 8080;

    private static final Logger log = LoggerFactory.getLogger(Util.class);

    /**
     * Convenience method for comparing two, possibly null, objects
     *
     * @return true if objects are both null or equal, false otherwise
     */
    public static boolean equals(final Object o1, final Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static boolean isLoggedIn(HttpSession session, String username, String password)
    {
        UserSession userSession = (UserSession) session.getAttribute(Constants.USERSESSION);
        if (userSession == null) {
            return false;
        }
        Customer customer = userSession.getCustomer();
        if (customer == null || customer.getId() < 1) {
            return false;
        }
        return customer.getUsername().equals(username) && customer.getPassword().equals(password);
    }

    public static String getCurrentPath(final ApplicationContext context) {
        String currPath = ".";

        try {
            currPath = context.getResource(".").getFile().getCanonicalPath();
        } catch (final IOException ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$ {0xD}
            , ex);
            try {
                currPath = new File(".").getCanonicalPath();
            } catch (final IOException ex1) {
                log.error("caught {} Error : ", ex1.getClass().getSimpleName() //$NON-NLS-1$ {0xD}
                , ex1);
            }
        }
        return currPath;
    }

    /**
     * Retrieve the HTTP host/port currently in use by Tomcat. Returns the default host/port if any errors occur during
     * retrieval.
     */
    public static HostPort getHostPort() {
        final HostPort results = new HostPort();
        try {
            results.setPort(getPort());
            results.setHost(InetAddress.getLocalHost().getHostName());
        } catch (final UnknownHostException ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1${0xD}
            , ex);
            results.setHost(InetAddress.getLoopbackAddress().getHostName());
        }
        return results;
    }

    /**
     * Retrieve the HTTP port currently in use by Tomcat. Returns the default port if any errors occur during retrieval.
     */
    public static String getHostPortString() {
        return getHostPort().toString();
    }

    /**
     * Retrieve the HTTP port currently in use by Tomcat. Returns the default port if any errors occur during retrieval.
     */
    public static int getPort() {
        try {
            final ArrayList<?> mBeanServers = MBeanServerFactory.findMBeanServer(null);
            if (mBeanServers != null && mBeanServers.size() > 0 && mBeanServers.get(0) instanceof MBeanServer) {
                final MBeanServer mBeanServer = (MBeanServer) mBeanServers.get(0);
                final ObjectName name = new ObjectName("Catalina", "type", "Server"); // parasoft-suppress
                                                                                      // CUSTOM.SLR
                                                                                      // "Class
                                                                                      // name"
                final Object server = mBeanServer.getAttribute(name, "managedResource"); // parasoft-suppress
                                                                                         // CUSTOM.SLR
                                                                                         // "Class
                                                                                         // name"
                if (server != null) {
                    final Method findServices = server.getClass().getMethod("findServices"); // parasoft-suppress
                                                                                             // CUSTOM.SLR
                                                                                             // "Method
                                                                                             // name"
                    final Object[] services = (Object[]) findServices.invoke(server);
                    for (final Object service : services) {
                        final Method findConnectors = service.getClass().getMethod("findConnectors"); // parasoft-suppress
                                                                                                      // CUSTOM.SLR
                                                                                                      // "Method
                                                                                                      // name"
                        final Object[] connectors = (Object[]) findConnectors.invoke(service);
                        for (final Object connector : connectors) {
                            final Method getProtocolHandler = connector.getClass().getMethod("getProtocolHandler"); // parasoft-suppress
                                                                                                                    // CUSTOM.SLR
                                                                                                                    // "Method
                                                                                                                    // name"
                            final Object protocolHandler = getProtocolHandler.invoke(connector);
                            final String handlerType = protocolHandler.getClass().getName();
                            if (handlerType.endsWith("Http11Protocol") // parasoft-suppress
                                // CUSTOM.SLR
                                // "Class
                                // name"
                                || handlerType.endsWith("Http11AprProtocol") // parasoft-suppress
                                                                             // CUSTOM.SLR
                                                                             // "Class
                                                                             // name"
                                || handlerType.endsWith("Http11NioProtocol")) { // parasoft-suppress
                                                                                // CUSTOM.SLR
                                                                                // "Class
                                                                                // name"
                                final Method getPort = connector.getClass().getMethod("getPort"); // parasoft-suppress
                                                                                                  // CUSTOM.SLR
                                                                                                  // "Method
                                                                                                  // name"
                                final Object port = getPort.invoke(connector);
                                if (port instanceof Integer) {
                                    return (Integer) port;
                                }
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            // We're not in Tomcat or something unexpected happened, so we
            // assume the default port
        }
        return -1;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Convenience method for testing if a object is null or (if string object is empty)</DD>
     * <DT>Date:</DT>
     * <DD>Oct 13, 2015</DD>
     * </DL>
     *
     * @param aAttribute
     *            the object to test
     * @return
     *         <DL>
     *         <DT><code>true</code></DT>
     *         <DD>the object is null or an empty string</DD>
     *         <DT><code>false</code></DT>
     *         <DD>otherwise</DD>
     *         </DL>
     */
    public static boolean isEmpty(final Object aAttribute) {
        return aAttribute == null
            || String.class.isAssignableFrom(aAttribute.getClass()) && isEmpty((String) aAttribute);
    }

    /**
     * Convenience method for testing if a string is null or empty
     *
     * @param string
     *            the string to test
     * @return true if string is null or empty, false otherwise
     */
    public static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add prepare description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 25, 2015</DD>
     * </DL>
     *
     * @param outputFileFolder
     * @throws IOException
     */
    public static void prepareFolder(final String outputFileFolder) throws IOException {
        final File directory = new File(outputFileFolder);
        if (!directory.isDirectory() && !directory.isFile()) {
            FileUtils.forceMkdir(directory);
        } else if (directory.isFile()) {
            try {
                FileUtils.forceDelete(directory);
                FileUtils.forceMkdir(directory);
            } catch (final Exception ex) {
                log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1${0xD}
                , ex);
            }
        }
    }

    private Util() {
    }

}
