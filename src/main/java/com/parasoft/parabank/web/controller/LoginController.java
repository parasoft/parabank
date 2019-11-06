package com.parasoft.parabank.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.Util;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.ViewUtil;

/**
 * Controller for looking up and logging in customer
 */
@Controller("/login.htm")
@RequestMapping("/login.htm")
public class LoginController extends AbstractBankController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @RequestMapping
    public ModelAndView handleRequest(@RequestParam("username") final String username,
        @RequestParam("password") final String password,
        @RequestParam(name = "forwardAction", required = false) final String forwardAction,
        @RequestParam(name = "ConnType", required = false) final String connType, final HttpSession session)
                throws Exception {
        //final String username = request.getParameter("username");
        //final String password = request.getParameter("password");
        Customer customer = null;
        final boolean connTypeSet = !Util.isEmpty(session.getAttribute("ConnType"));
        if (!connTypeSet) {
            if (!Util.isEmpty(connType)) {
                session.setAttribute("ConnType", connType);
            } else {
                session.setAttribute("ConnType", "JDBC");
            }
        }

        if (username == null || username.length() <= 0 || password == null || password.length() <= 0) {
            log.warn("Empty username and/or password used for login");
            return ViewUtil.createErrorView("error.empty.username.or.password");
        }

        if (!Util.isLoggedIn(session, username, password)) {
            // login function is handled by the appropriate access mode handler
            customer = accessModeController.login(username, password);

            if (customer == null) {
                log.warn("Invalid login attempt with username = " + username + " and password = " + password);
                return ViewUtil.createErrorView("error.invalid.username.or.password");
            }

            final UserSession userSession = new UserSession(customer);
            session.setAttribute(Constants.USERSESSION, userSession);
            //final String forwardAction = request.getParameter("forwardAction");
        }

        if (forwardAction != null) {
            log.info("Forwarding response to original request url: " + forwardAction);

            //response.sendRedirect(forwardAction);
            return new ModelAndView(new RedirectView(forwardAction, true));
        } else {
            return new ModelAndView(new RedirectView("/overview.htm", true));
            //response.sendRedirect("overview.htm");
            //return null;
        }
    }

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }
}
