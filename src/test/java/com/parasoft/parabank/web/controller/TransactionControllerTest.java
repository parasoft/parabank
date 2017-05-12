package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.mock.web.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.util.*;

public class TransactionControllerTest extends AbstractBankControllerTest<TransactionController> {

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setCommandClass(RequestLoanForm.class);
        setPath("/transaction.htm");
        setFormName("none");
        registerSession(request);
    }

    @Test
    public void testHandleInvalidRequest() throws Exception {
        try {
            processGetRequest(request, new MockHttpServletResponse());
            fail("expected exception (MissingServletRequestParameterException) not thrown");
        } catch (final Exception ex) {
            assertEquals("Required String parameter 'id' is not present", ex.getMessage());
            // this is good
        }
        request = registerSession(new MockHttpServletRequest());
        request.setParameter("id", "");
        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        //ModelAndView mav = controller.handleRequest(request, response);
        assertEquals("error", mav.getViewName());
        assertEquals("error.missing.transaction.id", getModelValue(mav, "message"));

        request = registerSession(new MockHttpServletRequest());
        request.setParameter("id", "str");
        mav = processGetRequest(request, new MockHttpServletResponse());
        //mav = controller.handleRequest(request, response);
        assertEquals("error", mav.getViewName());
        assertEquals("error.invalid.transaction.id", getModelValue(mav, "message"));

        //request.setParameter("id", "0");
        //mav = controller.handleRequest(request, response);
        request = registerSession(new MockHttpServletRequest());
        request.setParameter("id", "0");
        mav = processGetRequest(request, new MockHttpServletResponse());
        assertEquals("error.invalid.transaction.id", getModelValue(mav, "message"));
    }

    @Test
    public void testHandleRequest() throws Exception {
        request.setParameter("id", "12367");
        //final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        assertEquals(Constants.TRANSACTION, mav.getViewName());
        final Transaction transaction = (Transaction) mav.getModel().get(Constants.TRANSACTION);
        assertEquals(12367, transaction.getId());
    }
}
