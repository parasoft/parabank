package com.parasoft.parabank.web.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.util.AccessModeController;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO ChangeConnectionController is not used -- either eliminate or implement</DD>
 * <DT>Date:</DT>
 * <DD>Oct 10, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
@Controller
public class ChangeConnectionController extends AbstractBankController {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ChangeConnectionController.class);

    public ModelAndView handleRequest(@RequestParam("choice") final String selected, final HttpSession session)
            throws Exception {
        // String username = request.getParameter("username");
        // String password = request.getParameter("password");
        //final String selected = request.getParameter("choice");
        // log.info("choice = {} ",selected);
        //
        // This code is to set the default connection type as JDBC
        if (session.getAttribute("ConnType") == null) {
            session.setAttribute("ConnType", "JDBC");
        }
        if (selected != null) {
            session.setAttribute("ConnType", selected);
        }
        // log.info("ConnType = {} ",request.getSession().getAttribute("ConnType"));
        final ModelAndView mav = new ModelAndView(new RedirectView("/admin.htm", true));
        return mav;
        //response.sendRedirect("admin.htm");
        //return ViewUtil.createErrorView("error.invalid.username.or.password");
    }

    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //        // String username = request.getParameter("username");
    //        // String password = request.getParameter("password");
    //        final String selected = request.getParameter("choice");
    //        // System.out.println();
    //        // System.out.println(selected);
    //        // System.out.println();
    //        //
    //        // This code is to set the default connection type as JDBC
    //        if (request.getSession().getAttribute("ConnType") == null) {
    //            request.getSession().setAttribute("ConnType", "JDBC");
    //        }
    //        if (selected != null) {
    //            request.getSession().setAttribute("ConnType", selected);
    //        }
    //        // System.out.println(request.getSession().getAttribute("ConnType"));
    //        response.sendRedirect("admin.htm");
    //        return ViewUtil.createErrorView("error.invalid.username.or.password");
    //    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

}
