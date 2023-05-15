package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.util.Constants;

/**
 * @req PAR-34
 * @req PAR-14
 * @req PAR-30
 * @req PAR-29
 * @req PAR-28
 * @req PAR-27
 *
 */
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
            assertEquals("Required request parameter 'id' for method parameter type String is not present", ex.getMessage());
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
