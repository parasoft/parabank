package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.cxf.interceptor.Fault;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.parasoft.parabank.service.ParaBankServiceException;

/**
 * @req PAR-18
 * @req PAR-29
 * @req PAR-40
 */
public class ErrorControllerTest extends AbstractControllerTest<ErrorController> {
    private void assertError(final String message) throws Exception {
        //        final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processPostRequest(null, request, new MockHttpServletResponse());
        assertEquals("error", mav.getViewName());
        assertEquals(message, getModelValue(mav, "message"));
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/error.htm");
        setFormName("none");
        registerSession(request);
    }

    @Test
    public void testHandleRequest() throws Exception {
        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 404);
        assertError("error.not.found");

        request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, 500);
        assertError("error.internal");

        request = registerSession(new MockHttpServletRequest());
        request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE,
            new Exception(new Fault(new ParaBankServiceException("error message"))));
        //        final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processPostRequest(null, request, response);
        assertNull(mav);
        assertEquals(400, response.getStatus());
        assertEquals("error message", response.getContentAsString());
    }
}
