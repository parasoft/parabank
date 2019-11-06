package com.parasoft.parabank.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.Util;
import com.parasoft.parabank.web.ViewUtil;

/**
 * Controller for displaying transaction details
 */
@Controller("secure_transaction")
@RequestMapping("/transaction.htm")
public class TransactionController extends AbstractBankController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @RequestMapping
    public ModelAndView handleRequest(@RequestParam("id") final String id) throws Exception {

        //final String id = request.getParameter("id");
        if (Util.isEmpty(id)) {
            log.error("Missing required transaction id");
            return ViewUtil.createErrorView("error.missing.transaction.id");
        }

        Transaction transaction = null;
        try {

            String accessMode = null;

            if (adminManager != null) {
                accessMode = adminManager.getParameter("accessmode");
            }

            if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
                transaction = accessModeController.doGetTransaction(Integer.parseInt(id));
            }

            else {

                transaction = bankManager.getTransaction(Integer.parseInt(id));
                log.info("Using regular JDBC connection");
            }

        } catch (final NumberFormatException e) {
            log.error("Invalid transaction id = {}", id, e);
            return ViewUtil.createErrorView("error.invalid.transaction.id", new Object[] { id });
        } catch (final DataAccessException e) {
            log.error("Invalid transaction id = {}", id, e);
            return ViewUtil.createErrorView("error.invalid.transaction.id", new Object[] { id });
        }

        return new ModelAndView(Constants.TRANSACTION, Constants.TRANSACTION, transaction);
    }

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.TRANSACTION)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    //    @RequestMapping("/transaction.htm")
    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //
    //        final String id = request.getParameter("id");
    //        if (Util.isEmpty(id)) {
    //            log.error("Missing required transaction id");
    //            return ViewUtil.createErrorView("error.missing.transaction.id");
    //        }
    //
    //        Transaction transaction = null;
    //        try {
    //
    //            String accessMode = null;
    //
    //            if (adminManager != null) {
    //                accessMode = adminManager.getParameter("accessmode");
    //            }
    //
    //            if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //                transaction = accessModeController.doGetTransaction(Integer.parseInt(id));
    //            }
    //
    //            else {
    //
    //                transaction = bankManager.getTransaction(Integer.parseInt(id));
    //                log.info("Using regular JDBC connection");
    //            }
    //
    //        } catch (final NumberFormatException e) {
    //            log.error("Invalid transaction id = " + id, e);
    //            return ViewUtil.createErrorView("error.invalid.transaction.id",
    //                new Object[] { request.getParameter("id") });
    //        } catch (final DataAccessException e) {
    //            log.error("Invalid transaction id = " + id, e);
    //            return ViewUtil.createErrorView("error.invalid.transaction.id",
    //                new Object[] { request.getParameter("id") });
    //        }
    //
    //        return new ModelAndView(Constants.TRANSACTION, Constants.TRANSACTION, transaction);
    //    }

}
