package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.RequestLoanForm;

/**
 * @req PAR-24
 * @req PAR-16
 * @req PAR-32
 * @req PAR-18
 *
 */
@SuppressWarnings({ "unchecked" })
public class RequestLoanControllerTest extends AbstractBankControllerTest<RequestLoanController> {

    @Resource(name = "adminManager")
    AdminManager adminManager;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setCommandClass(RequestLoanForm.class);
        setPath("/requestloan.htm");
        setFormName(Constants.REQUESTLOANFORM);
        registerSession(request);
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        //final ModelAndView mav = controller.handleRequest(request, response);
        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
        assertEquals(11, accounts.size());
    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final RequestLoanForm form = (RequestLoanForm) mav.getModel().get(getFormName());
        final int fromAccountId = 12345;
        form.setAmount(new BigDecimal("1000.00"));
        form.setDownPayment(new BigDecimal("100.00"));
        form.setFromAccountId(fromAccountId);
        //adminManager.setParameter(name, value);
        request = registerSession(new MockHttpServletRequest());

        mav = processPostRequest(form, request, new MockHttpServletResponse());
        //final BindException errors = new BindException(form, Constants.REQUESTLOANFORM);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        assertEquals("requestloanConfirm", mav.getViewName());

        final LoanResponse response = (LoanResponse) mav.getModel().get("loanResponse");
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());
        assertTrue(response.getAccountId() > fromAccountId);
    }

    @Test
    public void testValidate() throws Exception {
        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final RequestLoanForm form = (RequestLoanForm) mav.getModel().get(getFormName());
        //final RequestLoanForm form = new RequestLoanForm();
        form.setAmount(null);
        request = registerSession(new MockHttpServletRequest());
        mav = processPostRequest(form, request, new MockHttpServletResponse());
        //final BindException errors = new BindException(form, Constants.REQUESTLOANFORM);
        //controller.onBindAndValidate(request, form, errors);
        //log.info ("Got ModelAndView {}",mav);
        getErrorMap().clear();
        getErrorMap().put("amount", "error.loan.amount.empty");
        getErrorMap().put("downPayment", "error.down.payment.empty");
        assertError(mav, getErrorMap());

        //        assertEquals(2, errors.getErrorCount());
        //        assertNotNull(errors.getFieldError("amount"));
        //        assertNotNull(errors.getFieldError("downPayment"));
    }
    //"amount", "error.loan.amount.empty"
    //"downPayment", "error.down.payment.empty"
}
