package com.parasoft.parabank.util;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO add description</DD>
 * <DT>Date:</DT>
 * <DD>Oct 9, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public class HostPort {
    private String host;

    private int port;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HostPort Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     */
    public HostPort() {

    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>HostPort Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aHost
     * @param aPort
     */
    public HostPort(final String aHost, final int aPort) {
        super();
        host = aHost;
        port = aPort;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the host property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of host field
     */
    public String getHost() {
        return host;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the port property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of port field
     */
    public int getPort() {
        return port;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Test whether the host has been set</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return
     *         <DL>
     *         <DT><code>true</code></DT>
     *         <DD>The host is not null or empty</DD>
     *         <DT><code>false</code></DT>
     *         <DD>otherwise</DD>
     *         </DL>
     */
    public boolean isHostSet() {
        return host != null && host != "";
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Test whether the port has been set</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return
     *         <DL>
     *         <DT><code>true</code></DT>
     *         <DD>The port is greater than 1</DD>
     *         <DT><code>false</code></DT>
     *         <DD>otherwise</DD>
     *         </DL>
     */
    public boolean isPortSet() {
        return port > 1;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the host property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aHost
     *            new value for the host property
     */
    public void setHost(final String aHost) {
        host = aHost;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the port property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aPort
     *            new value for the port property
     */
    public void setPort(final int aPort) {
        port = aPort;
    }

    /**
     * {@inheritDoc}
     * <DL>
     * <DT>Description:</DT>
     * <DD>This will return formatted host:port string. if host is null or empty
     * "localhost" will be used if port is not set(0/-1) 8080 will be used for
     * port.</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return formatted host:port string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(isHostSet() ? getHost() : "localhost");
        sb.append(":").append(isPortSet() ? getPort() : 8080);
        return sb.toString();
    }
}
