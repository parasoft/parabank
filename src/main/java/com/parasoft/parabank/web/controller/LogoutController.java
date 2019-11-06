package com.parasoft.parabank.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.util.AccessModeController;

/**
 * Controller for logging out a customer
 */
@Controller("/logout.htm")
@RequestMapping("/logout.htm")
public class LogoutController extends AbstractBankController {

    @RequestMapping
    public ModelAndView handleRequest(final HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);
        Object connType = "JDBC";
        if (session != null) {
            connType = session.getAttribute("ConnType");
            //session.removeAttribute(Constants.USERSESSION); redundant the next statement does this auto-magically ;-)
            session.invalidate();
        }
        session = request.getSession(true);
        //request.getSession().setAttribute("ConnType",connType );
        //final ServletRequestAttributes attr =
        //(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        //final HttpSession newSession = attr.getRequest().getSession();
        session.setAttribute("ConnType", connType);
        final ModelAndView mav = new ModelAndView(new RedirectView("/index.htm", true));
        mav.addObject("ConnType", connType);
        //response.sendRedirect("index.htm");
        return mav;
    }
    //    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //        request.getSession().removeAttribute(Constants.USERSESSION);
    //        request.getSession().invalidate();
    //        request.getSession().setAttribute("ConnType", request.getSession().getAttribute("ConnType"));
    //        response.sendRedirect("index.htm");
    //        return null;
    //    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // TODO Auto-generated method stub

    }
}
