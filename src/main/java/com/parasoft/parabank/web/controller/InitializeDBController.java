package com.parasoft.parabank.web.controller;

import javax.annotation.*;

import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;

import com.parasoft.parabank.dao.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.*;

/**
 * Controller for creating database tables and populating with sample data
 */
@Controller("/initializeDB.htm")
@RequestMapping("/initializeDB.htm")
public class InitializeDBController extends AbstractBankController {
    private static final Logger log = LoggerFactory.getLogger(InitializeDBController.class);

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Resource(name = "bookstoreDao")
    private AdminDao bookstoreAdminDao;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the adminManager property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 19, 2015</DD>
     * </DL>
     *
     * @return the value of adminManager field
     */
    public AdminManager getAdminManager() {
        return adminManager;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the bookstoreAdminDao property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 19, 2015</DD>
     * </DL>
     *
     * @return the value of bookstoreAdminDao field
     */
    public AdminDao getBookstoreAdminDao() {
        return bookstoreAdminDao;
    }

    @RequestMapping
    public ModelAndView handleRequest() throws Exception {
        ModelAndView mav;
        try {
            adminManager.initializeDB();
            bookstoreAdminDao.initializeDB();
            mav = new ModelAndView(new RedirectView("/index.htm", true));
            //response.sendRedirect("index.htm");
        } catch (final Throwable t) {
            log.error("Error initializing database", t);
            return ViewUtil.createErrorView("error.could.not.initialize.database");
        }
        return mav;
    }

    //    @Override
    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //        try {
    //            adminManager.initializeDB();
    //            bookstoreAdminDao.initializeDB();
    //            response.sendRedirect("index.htm");
    //        } catch (final Throwable t) {
    //            log.error("Error initializing database", t);
    //            return ViewUtil.createErrorView("error.could.not.initialize.database");
    //        }
    //        return null;
    //    }
    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public void setBookstoreAdminDao(final AdminDao bookstoreAdminDao) {
        this.bookstoreAdminDao = bookstoreAdminDao;
    }
}
