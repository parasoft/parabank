package com.parasoft.parabank.web.controller;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.Util;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * Controller for starting/stopping JMS listener
 */
@Controller("/jms.htm")
@RequestMapping("/jms.htm")
public class JmsListenerController extends AbstractBaseAdminController implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(JmsListenerController.class);

    @Override
    @PreDestroy
    public void destroy() {
        try {
            log.info("JMS Broker Shutdown sequence initiated");
            getAdminManager().shutdownJmsListener();
            log.info("JMS Broker Shutdown sequence completed");
        } catch (final Exception ex) {
            log.error("caught {} Error : ", ex.getClass().getSimpleName() //$NON-NLS-1$
                , ex);
        }
    }
    @Override
    @ModelAttribute(Constants.ADMINFORM)
    public AdminForm getForm() throws Exception {
        return super.getForm();
    }

    @RequestMapping
    public ModelAndView handleRequest(@RequestParam("shutdown") final String sShutdown, final Model model)
            throws Exception {
        //final String sShutdown = request.getParameter("shutdown");
        final ModelAndView modelAndView = new ModelAndView(getFormView(), model.asMap());
        if (Util.isEmpty(sShutdown)) {
            log.warn("Empty shutdown parameter");
            modelAndView.addObject("error", "error.empty.shutdown.parameter");
            //return ViewUtil.createErrorView("error.empty.shutdown.parameter");
        } else {
            final boolean shutdown = Boolean.parseBoolean(sShutdown);
            //        String connType = request.getSession().getAttribute("ConnType").toString();
            if (shutdown) { //  amc.doJmsShutdown(connType);
                getAdminManager().shutdownJmsListener();
                log.info("Using regular JDBC connection. AccessModeController not implemented.");
                modelAndView.addObject("message", "jms.shutdown.success");
            } else {
                //  amc.doJmsStartup(connType);
                getAdminManager().startupJmsListener();
                log.info("Using regular JDBC connection. AccessModeController not implemented.");
                modelAndView.addObject("message", "jms.startup.success");
            }
            //response.sendRedirect("admin.htm");
        }
        modelAndView.setView(new RedirectView("/admin.htm", true));
        return modelAndView;
    }

    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //        final String sShutdown = request.getParameter("shutdown");
    //
    //        if (Util.isEmpty(sShutdown)) {
    //            log.warn("Empty shutdown parameter");
    //            return ViewUtil.createErrorView("error.empty.shutdown.parameter");
    //        }
    //
    //        final boolean shutdown = Boolean.parseBoolean(sShutdown);
    //        //        String conntype = request.getSession().getAttribute("ConnType").toString();
    //        if (shutdown) {
    //
    //            //    amc.doJmsShutdown(conntype);
    //            adminManager.shutdownJmsListener();
    //            log.info("Using regular JDBC connection");
    //
    //            //             if (conntype.equals("SOAP")) {
    //            //                 URL url = new URL(
    //            //                         "http://localhost:8080/parabank/services/ParaBank?wsdl");
    //            //                 QName qname = new QName("http://service.parabank.parasoft.com/",
    //            //                         "ParaBank");
    //            //                 Service service = Service.create(url, qname);
    //            //                 ParaBankService obj = service.getPort(ParaBankService.class);
    //            //             obj.shutdownJmsListener();
    //            //                 log.info("Using SOAP Web Service: ParaBank");
    //            //             }
    //            //
    //            //             else if (conntype.equals("RESTXML")) {
    //            //
    //            //                 URL url1 = new URL(
    //            //                         "http://localhost:8080/parabank/services/bank/shutdownJmsListener");
    //            //
    //            //                 HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
    //            //                 conn.setRequestMethod("POST");
    //            //                 conn.setRequestProperty("Accept", "application/xml");
    //            //                 //JAXBContext jc = JAXBContext.newInstance(Customer.class);
    //            //                 InputStream xml = conn.getInputStream();
    //            //                 //customer = (Customer) jc.createUnmarshaller().unmarshal(xml);
    //            //
    //            //                 conn.disconnect();
    //            //
    //            //                 log.info("Using REST xml Web Service: Bank");
    //            //             } else if (conntype.equals("RESTJSON")) {
    //            //
    //            //                 URL url1 = new URL(
    //            //                         "http://localhost:8080/parabank/services/bank/shutdownJmsListener");
    //            //
    //            //                 HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
    //            //                 conn.setRequestMethod("POST");
    //            //                 conn.setRequestProperty("Accept", "application/json");
    //            //
    //            //                 customer = (new ObjectMapper()).readValue(conn.getInputStream(), Customer.class);
    //            //                 conn.disconnect();
    //            //                 log.info("Using REST json Web Service: Bank");
    //            //             }
    //            //             else {
    //            //
    //            //
    //            //                 adminManager.shutdownJmsListener();
    //            //                      log.info("Using regular JDBC connection");
    //            //                 }
    //
    //        } else {
    //
    //            //    amc.doJmsStartup(conntype);
    //            adminManager.startupJmsListener();
    //            log.info("Using regular JDBC connection");
    //            //            if (conntype.equals("SOAP")) {
    //            //                 URL url = new URL(
    //            //                         "http://localhost:8080/parabank/services/ParaBank?wsdl");
    //            //                 QName qname = new QName("http://service.parabank.parasoft.com/",
    //            //                         "ParaBank");
    //            //                 Service service = Service.create(url, qname);
    //            //                 ParaBankService obj = service.getPort(ParaBankService.class);
    //            //             obj.startupJmsListener();
    //            //                 log.info("Using SOAP Web Service: ParaBank");
    //            //             }
    //            //
    //            //             else if (conntype.equals("RESTXML")) {
    //            //
    //            //                 URL url1 = new URL(
    //            //                         "http://localhost:8080/parabank/services/bank/startupJmsListener");
    //            //
    //            //                 HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
    //            //                 conn.setRequestMethod("POST");
    //            //                 conn.setRequestProperty("Accept", "application/xml");
    //            //                 //JAXBContext jc = JAXBContext.newInstance(Customer.class);
    //            //                 InputStream xml = conn.getInputStream();
    //            //                 //customer = (Customer) jc.createUnmarshaller().unmarshal(xml);
    //            //
    //            //                 conn.disconnect();
    //            //
    //            //                 log.info("Using REST xml Web Service: Bank");
    //            //             } else if (conntype.equals("RESTJSON")) {
    //            //
    //            //                 URL url1 = new URL(
    //            //                         "http://localhost:8080/parabank/services/bank/startupJmsListener");
    //            //
    //            //                 HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
    //            //                 conn.setRequestMethod("POST");
    //            //                 conn.setRequestProperty("Accept", "application/json");
    //            //
    //            //                 customer = (new ObjectMapper()).readValue(conn.getInputStream(), Customer.class);
    //            //                 conn.disconnect();
    //            //                 log.info("Using REST json Web Service: Bank");
    //            //             }
    //            //             else {
    //            //
    //            //
    //            //                  adminManager.startupJmsListener();
    //            //                      log.info("Using regular JDBC connection");
    //            //                 }
    //            //
    //        }
    //
    //        response.sendRedirect("admin.htm");
    //        return null;
    //    }

}
