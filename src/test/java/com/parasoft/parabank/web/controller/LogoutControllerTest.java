package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;

/**
 * @req PAR-27
 * @req PAR-45
 * @req PAR-43
 * @req PAR-4
 * @req PAR-2
 * @req PAR-3
 *
 */
public class LogoutControllerTest extends AbstractControllerTest<LogoutController> {
    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/logout.htm");
        setFormName("none");
        registerSession(request);
        //        final MockHttpSession session = new MockHttpSession();
        //        final Customer customer = new Customer();
        //        customer.setId(1);
        //        session.setAttribute(Constants.USERSESSION, new UserSession(customer));
        //        request.setSession(session);
    }

    @Test
    public void testHandleRequest() throws Exception {
        UserSession session = (UserSession) request.getSession().getAttribute(Constants.USERSESSION);
        assertNotNull(session);
        //RequestContextListener
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());

        //final ModelAndView mav = controller.handleRequest(request, response);
        assertNotNull(mav);
        // assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
        assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
        //        assertNull(mav);
        //        assertEquals("index.htm", response.getRedirectedUrl());
        session = (UserSession) request.getSession().getAttribute(Constants.USERSESSION);
        assertNull(session);
    }
}
