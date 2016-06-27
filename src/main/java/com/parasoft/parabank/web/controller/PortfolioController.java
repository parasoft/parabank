package com.parasoft.parabank.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.util.AccessModeController;

@Controller("/portfolio.htm")
@RequestMapping("/portfolio.htm")
public class PortfolioController extends AbstractBankController {

    @RequestMapping
    public ModelAndView handleRequest() throws Exception {
        return new ModelAndView("portfolio");
    }
    //    public ModelAndView handleRequest(final HttpServletRequest arg0, final HttpServletResponse arg1) throws Exception {
    //        return new ModelAndView("portfolio");
    //    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

}
