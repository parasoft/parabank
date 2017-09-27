package com.parasoft.parabank.messaging;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import javax.annotation.*;
import javax.management.*;
import javax.xml.namespace.*;
import javax.xml.ws.*;

import org.slf4j.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.service.*;
import com.parasoft.parabank.util.*;

/**
 * Message client for generating and sending loan requests over SOAP
 */
public class WebServiceLoanProvider implements LoanProvider {
    private static final Logger log = LoggerFactory.getLogger(WebServiceLoanProvider.class);

    private static final String NL_LOCALHOST = "localhost";

    /**
     * Retrieve the HTTP port currently in use by Tomcat. Returns the default port if any errors occur during retrieval.
     */
    private static int getPort() {
        try {
            final ArrayList<?> mBeanServers = MBeanServerFactory.findMBeanServer(null);
            if (mBeanServers != null && mBeanServers.size() > 0 && mBeanServers.get(0) instanceof MBeanServer) {
                final MBeanServer mBeanServer = (MBeanServer) mBeanServers.get(0);
                final ObjectName name = new ObjectName("Catalina", "type", "Server"); // parasoft-suppress CUSTOM.SLR  "Class name"
                final Object server = mBeanServer.getAttribute(name, "managedResource"); // parasoft-suppress CUSTOM.SLR "Class name"
                if (server != null) {
                    final Method findServices = server.getClass().getMethod("findServices"); // parasoft-suppress CUSTOM.SLR "Method name"
                    final Object[] services = (Object[]) findServices.invoke(server);
                    for (final Object service : services) {
                        final Method findConnectors = service.getClass().getMethod("findConnectors"); // parasoft-suppress CUSTOM.SLR "Method name"
                        final Object[] connectors = (Object[]) findConnectors.invoke(service);
                        for (final Object connector : connectors) {
                            final Method getProtocolHandler = connector.getClass().getMethod("getProtocolHandler"); // parasoft-suppress CUSTOM.SLR "Method name"
                            final Object protocolHandler = getProtocolHandler.invoke(connector);
                            final String handlerType = protocolHandler.getClass().getName();
                            if (handlerType.endsWith("Http11Protocol") // parasoft-suppress CUSTOM.SLR "Class name"
                                || handlerType.endsWith("Http11AprProtocol") // parasoft-suppress CUSTOM.SLR "Class name"
                                || handlerType.endsWith("Http11NioProtocol")) { // parasoft-suppress CUSTOM.SLR "Class name"
                                final Method getPort = connector.getClass().getMethod("getPort"); // parasoft-suppress CUSTOM.SLR "Method name"
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

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    private String wsdlUrl;

    @Override
    public LoanResponse requestLoan(final LoanRequest loanRequest) {
        try {
            // If wsdlUrl is localhost, replace the port in the WSDL with the
            // runtime HTTP port
            final String[] wsdlUrlSplit = wsdlUrl.split("/");
            if (wsdlUrlSplit.length >= 3 && !Util.isEmpty(wsdlUrlSplit[2])
                && wsdlUrlSplit[2].startsWith(NL_LOCALHOST)) {
                final int currentPort = getPort();
                if (currentPort > 0) {
                    wsdlUrl = wsdlUrl.replaceFirst(wsdlUrlSplit[2], NL_LOCALHOST + ":" + currentPort);
                }
            }
            final URL wsdlURL = new URL(wsdlUrl);
            final QName serviceName = new QName(LoanProcessorService.TNS, "LoanProcessorServiceImplService"); // parasoft-suppress CUSTOM.SLR "Class name"
            final Service service = Service.create(wsdlURL, serviceName);
            final LoanProcessorService client = service.getPort(LoanProcessorService.class);

            final String endpoint = adminManager.getParameter(AdminParameters.ENDPOINT);
            if (!Util.isEmpty(endpoint) && client instanceof BindingProvider) {
                ((BindingProvider) client).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
            }

            return client.requestLoan(loanRequest);
        } catch (final MalformedURLException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
        } catch (final ParaBankServiceException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
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
