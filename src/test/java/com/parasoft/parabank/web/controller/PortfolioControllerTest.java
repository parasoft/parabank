package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class PortfolioControllerTest extends AbstractControllerTest<PortfolioController> {

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //        controller.setCommandClass(OpenAccountForm.class);
        //        controller.setAdminManager(adminManager);
        setPath("/portfolio.htm");
        setFormName("none");
        registerSession(request);

    }

    @Test
    public void testHandleRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        //final ModelAndView mav = controller.handleRequest(null, null);
        assertEquals("portfolio", mav.getViewName());
        assertNotNull(mav.getModel());
    }

}
