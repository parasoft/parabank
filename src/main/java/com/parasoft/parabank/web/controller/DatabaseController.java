package com.parasoft.parabank.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.Util;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * Controller for manipulating database entries
 */
@Controller("/db.htm")
@RequestMapping("/db.htm")
public class DatabaseController extends AbstractBaseAdminController {
    @Override
    @ModelAttribute(Constants.ADMINFORM)
    public AdminForm getForm() throws Exception {
        return super.getForm();
    }

    /**
     * @param request
     * @param response
     * @throws java.lang.Exception
     * @return
     *
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView handleRequest(@RequestParam("action") final String action, final Model model) throws Exception {
        // final String action = request.getParameter("action");
        final ModelAndView modelAndView = new ModelAndView(getFormView());
        modelAndView.addAllObjects(model.asMap());
        // String connType = request.getSession().getAttribute("ConnType").toString();

        // WebService code starts here
        //  log.info("connType = {} ",connType);
        final AdminForm form = (AdminForm) model.asMap().get(getCommandName());

        if (Util.isEmpty(action)) {
            log.warn("Empty action parameter");
            modelAndView.addObject("error", "error.invalid.action.parameter");
        } else if ("INIT".equalsIgnoreCase(action)) {
            getAdminManager().initializeDB();
            //log.info("Using regular JDBC connection. AccessModeController not implemented.");
            saveAdminSettings(form);
            modelAndView.addObject("message", "database.initialize.success");
        } else if ("CLEAN".equalsIgnoreCase(action)) {
            getAdminManager().cleanDB();
            saveAdminSettings(form);
            //log.info("Using regular JDBC connection. AccessModeController not implemented.");
            modelAndView.addObject("message", "database.clean.success");
        } else {
            log.warn("Unrecognized database action: {}", action);
            modelAndView.addObject("error", "error.invalid.action.parameter");
        }

        // response.sendRedirect("admin.htm");
        return modelAndView;
    }

    //    /**
    //     * @param request
    //     * @param response
    //     * @throws java.lang.Exception
    //     * @return
    //     *
    //     */
    //    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response)
    //            throws Exception {
    //        final String action = request.getParameter("action");
    //
    //        if (Util.isEmpty(action)) {
    //            log.warn("Empty action parameter");
    //            return ViewUtil.createErrorView("error.empty.action.parameter");
    //        }
    //
    //        // String connType =
    //        // request.getSession().getAttribute("ConnType").toString();
    //
    //        // WebService code starts here
    //        // log.info("connType = {} ",connType);
    //        if ("INIT".equals(action)) {
    //
    //            adminManager.initializeDB();
    //            log.info("Using regular JDBC connection");
    //
    //        } else if ("CLEAN".equals(action)) {
    //            adminManager.cleanDB();
    //            log.info("Using regular JDBC connection");
    //
    //        } else {
    //            log.warn("Unrecognized database action: " + action);
    //            return ViewUtil.createErrorView("error.invalid.action.parameter");
    //        }
    //
    //        response.sendRedirect("admin.htm");
    //        return null;
    //    }

}
