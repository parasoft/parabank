package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;

/**
 * @req PAR-3
 * @req PAR-18
 * @req PAR-39
 *
 */
public class LoginControllerTest extends AbstractBankControllerTest<LoginController> {

    public void assertError(final String message) throws Exception {
        //final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        assertEquals("error", mav.getViewName());
        assertEquals(message, getModelValue(mav, "message"));
    }

    public void assertGetRequest(final String url) throws Exception {
        request.setParameter("username", "john");
        request.setParameter("password", "demo");
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        assertNotNull(mav);
        // assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
        assertEquals(url, ((RedirectView) mav.getView()).getUrl());
        //final ModelAndView mav = controller.handleRequest(request, response);
        //assertNull(mav);
        //assertEquals(url, response.getRedirectedUrl());
        final UserSession session = (UserSession) request.getSession().getAttribute(Constants.USERSESSION);
        assertNotNull(session);
        assertEquals(12212, session.getCustomer().getId());
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setAccessModeController(amc);
        setPath("/login.htm");
        setFormName("none");
        //registerSession(request);
    }

    @Test
    public void testHandleBadGetRequest() throws Exception {
        try {
            assertError("error.empty.username.or.password");
            fail("expected exception (MissingServletRequestParameterException) not thrown");
        } catch (final Exception ex) {
            assertEquals("Required request parameter 'username' for method parameter type String is not present", ex.getMessage());
            // this is good
        }

        try {
            request = new MockHttpServletRequest();
            request.setParameter("username", "user");
            assertError("error.empty.username.or.password");
            fail("expected exception (MissingServletRequestParameterException) not thrown");
        } catch (final Exception ex) {
            assertEquals("Required request parameter 'password' for method parameter type String is not present", ex.getMessage());
            // this is good
        }

        request = new MockHttpServletRequest();
        request.setParameter("username", "");
        request.setParameter("password", "pass");
        assertError("error.empty.username.or.password");

        request = new MockHttpServletRequest();
        request.setParameter("username", "user");
        request.setParameter("password", "pass");
        assertError("error.invalid.username.or.password");
    }

    @Test
    public void testHandleForward() throws Exception {
        assertGetRequest("/overview.htm");
    }

    @Test
    public void testHandleRedirect() throws Exception {
        request.setParameter("forwardAction", "/page.htm");
        assertGetRequest("/page.htm");
    }
}
