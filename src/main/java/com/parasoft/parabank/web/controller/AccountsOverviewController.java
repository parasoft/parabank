package com.parasoft.parabank.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;

/**
 * Controller for displaying all user accounts
 */
@Controller("secure_overview") @RequestMapping("/overview.htm") public class AccountsOverviewController
        extends AbstractBankController {
    private static final Logger log = LoggerFactory.getLogger(AccountsOverviewController.class);

    @Resource(name = "accessModeController") private AccessModeController accessModeController;

    @Resource(name = "adminManager") private AdminManager adminManager;

    //    @RequestMapping(method = RequestMethod.GET)
    //    public ModelAndView handleRequest(final HttpServletRequest request) throws Exception {
    //
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    @RequestMapping
    public ModelAndView handleRequest(@SessionParam(Constants.USERSESSION) final UserSession userSession)
            throws Exception {
        //
        //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);

        final Customer customer = userSession.getCustomer();

        List<Account> accounts;// = new ArrayList<Account>();

        String accessMode = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }

        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accounts = accessModeController.doGetAccounts(customer);
        } else {
            accounts = bankManager.getAccountsForCustomer(customer);
            log.warn("Using regular JDBC connection");
        }

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalAvailableBalance = BigDecimal.ZERO;

        for (final Account account : accounts) {
            totalBalance = totalBalance.add(account.getBalance());
            totalAvailableBalance = totalAvailableBalance.add(account.getAvailableBalance());
        }

        final Map<String, Object> model = new HashMap<>();
        model.put("customerId", customer.getId());
        return new ModelAndView("overview", "model", model);
    }

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //
    //        final Log log = LogFactory.getLog(AccountsOverviewController.class);
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //
    //        final Customer customer = userSession.getCustomer();
    //
    //        List<Account> accounts = new ArrayList<Account>();
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            accounts = accessModeController.doGetAccounts(customer);
    //        } else {
    //            accounts = bankManager.getAccountsForCustomer(customer);
    //            log.warn("Using regular JDBC connection");
    //        }
    //
    //        BigDecimal totalBalance = BigDecimal.ZERO;
    //        BigDecimal totalAvailableBalance = BigDecimal.ZERO;
    //
    //        for (final Account account : accounts) {
    //            totalBalance = totalBalance.add(account.getBalance());
    //            totalAvailableBalance = totalAvailableBalance.add(account.getAvailableBalance());
    //        }
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("accounts", accounts);
    //        model.put("totalBalance", totalBalance);
    //        model.put("totalAvailableBalance", totalAvailableBalance);
    //
    //        return new ModelAndView("overview", "model", model);
    //    }

}
