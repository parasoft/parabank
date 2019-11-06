package com.parasoft.parabank.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.test.util.AbstractParaBankTest;
import com.parasoft.parabank.util.Constants;

/**
 * @req PAR-46
 *
 */
public class LoginInterceptorTest extends AbstractParaBankTest {
    private static final String SERVLET_PATH = "/test.htm";

    private static final String QUERY_STRING = "param=value";

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private ModelAndView assertLoginForm(final LoginInterceptor interceptor) throws Exception {
        try {
            assertFalse(interceptor.preHandle(request, response, null));
            fail("Did not catch expected ModelAndViewDefiningException");
        } catch (final ModelAndViewDefiningException e) {
            final ModelAndView mav = e.getModelAndView();
            assertEquals(Constants.LOGINFORM, mav.getViewName());
            return mav;
        }

        return null;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        request = new MockHttpServletRequest();
        request.setServletPath(SERVLET_PATH);
        response = new MockHttpServletResponse();
    }

    /**
     * @req PAR-2
     * @throws Exception
     */
    @Test
    public void testLoginInterceptor() throws Exception {
        final LoginInterceptor interceptor = new LoginInterceptor();

        ModelAndView mav = assertLoginForm(interceptor);
        assertEquals(SERVLET_PATH, mav.getModel().get("loginForwardAction"));

        request.setQueryString(QUERY_STRING);
        mav = assertLoginForm(interceptor);
        assertEquals(SERVLET_PATH + "?" + QUERY_STRING, mav.getModel().get("loginForwardAction"));

        final MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        assertLoginForm(interceptor);

        session.setAttribute(Constants.USERSESSION, new UserSession(new Customer()));
        assertTrue(interceptor.preHandle(request, response, null));
    }
}
