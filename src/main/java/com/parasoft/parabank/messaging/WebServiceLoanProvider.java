package com.parasoft.parabank.messaging;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.AdminParameters;
import com.parasoft.parabank.domain.logic.LoanProvider;
import com.parasoft.parabank.service.LoanProcessorService;
import com.parasoft.parabank.service.ParaBankServiceException;
import com.parasoft.parabank.util.Util;

/**
 * Message client for generating and sending loan requests over SOAP
 */
public class WebServiceLoanProvider implements LoanProvider {
    private static final Logger log = LoggerFactory.getLogger(WebServiceLoanProvider.class);

    private static final String NL_LOCALHOST = "localhost"; //$NON-NLS-1$

    /**
     * Retrieve the URI currently in use by Tomcat. Returns null if any errors occur during retrieval.
     */
    private static URI getLocalUri(String path, String query) {
        URI ret = null;
        try {
            final ArrayList<?> mBeanServers = MBeanServerFactory.findMBeanServer(null);
            if (mBeanServers != null && mBeanServers.size() > 0 && mBeanServers.get(0) instanceof MBeanServer) {
                final MBeanServer mBeanServer = (MBeanServer) mBeanServers.get(0);
                final ObjectName name = new ObjectName("Catalina", "type", "Server"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                final Object server = mBeanServer.getAttribute(name, "managedResource"); //$NON-NLS-1$
                if (server != null) {
                    final Method findServices = server.getClass().getMethod("findServices"); //$NON-NLS-1$
                    final Object[] services = (Object[]) findServices.invoke(server);
                    for (final Object service : services) {
                        final Method findConnectors = service.getClass().getMethod("findConnectors"); //$NON-NLS-1$
                        final Object[] connectors = (Object[]) findConnectors.invoke(service);
                        for (final Object connector : connectors) {
                            final Method getProtocolHandler = connector.getClass().getMethod("getProtocolHandler"); //$NON-NLS-1$
                            final Object protocolHandler = getProtocolHandler.invoke(connector);
                            final String handlerType = protocolHandler.getClass().getName();
                            if (handlerType.endsWith("Http11Protocol") //$NON-NLS-1$
                                || handlerType.endsWith("Http11AprProtocol") //$NON-NLS-1$
                                || handlerType.endsWith("Http11NioProtocol")) { //$NON-NLS-1$
                                final Method getPort = connector.getClass().getMethod("getPort"); //$NON-NLS-1$
                                final Object port = getPort.invoke(connector);
                                if (port instanceof Integer) {
                                    final Method getScheme = connector.getClass().getMethod("getScheme"); //$NON-NLS-1$
                                    Object schemeObj = getScheme.invoke(connector);
                                    if (schemeObj instanceof String) {
                                        String scheme = (String) schemeObj;
                                        // favor http over https
                                        if (ret == null || "http".equalsIgnoreCase(scheme)) { //$NON-NLS-1$
                                            ret = new URI(scheme, null, NL_LOCALHOST, (Integer) port, path, query, null);
                                        }
                                    }
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
        return ret;
    }

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    private String wsdlUrl;

    @Override
    public LoanResponse requestLoan(final LoanRequest loanRequest) {
        try {
            // If wsdlUrl is localhost, replace the port in the WSDL with the
            // runtime HTTP port
            URL wsdlURL = new URL(wsdlUrl);
            if (NL_LOCALHOST.equalsIgnoreCase(wsdlURL.getHost())) {
                URI localUri = getLocalUri(wsdlURL.getPath(), wsdlURL.getQuery());
                if (localUri != null) {
                    wsdlURL = localUri.toURL();
                }
            }
            final QName serviceName = new QName(LoanProcessorService.TNS, "LoanProcessorServiceImplService"); //$NON-NLS-1$
            final Service service = Service.create(wsdlURL, serviceName);
            final LoanProcessorService client = service.getPort(LoanProcessorService.class);

            final String endpoint = adminManager.getParameter(AdminParameters.ENDPOINT);
            if (!Util.isEmpty(endpoint) && client instanceof BindingProvider) {
                ((BindingProvider) client).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
            }

            return client.requestLoan(loanRequest);
        } catch (final MalformedURLException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e); //$NON-NLS-1$
        } catch (final ParaBankServiceException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e); //$NON-NLS-1$
        }
        return null;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public void setWsdlUrl(final String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }
}
