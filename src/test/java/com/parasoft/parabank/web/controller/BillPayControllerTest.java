package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.domain.validator.AddressValidator;
import com.parasoft.parabank.domain.validator.PayeeValidator;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.BillPayForm;

/**
 * @req PAR-26
 * @author sang
 *
 */
@SuppressWarnings({ "unchecked" })
public class BillPayControllerTest extends AbstractValidatingBankControllerTest<BillPayController> {

    private void assertReferenceData(final ModelAndView mav) {
        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
        assertEquals(11, accounts.size());
    }

    private BillPayForm getBillPayForm() {
        final BillPayForm form = new BillPayForm();
        final Payee payee = new Payee();
        payee.setName("payee name");
        final Address address = new Address();
        address.setStreet("payee street");
        address.setCity("payee city");
        address.setState("payee state");
        address.setZipCode("payee zipcode");
        payee.setAddress(address);
        payee.setPhoneNumber("payee phone number");
        payee.setAccountNumber(100);
        form.setPayee(payee);
        form.setVerifyAccount(100);
        form.setAmount(new BigDecimal("100.0"));
        form.setFromAccountId(12345);
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        final PayeeValidator validator = new PayeeValidator();
        validator.setAddressValidator(new AddressValidator());
        controller.setValidator(validator);
        controller.setMessageSource(getApplicationContext());
        setPath("/billpay.htm");
        setFormName(Constants.BILLPAYFORM);
        registerSession(request);
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final BillPayForm form = (BillPayForm) mav.getModel().get(getFormName());
        assertNotNull("BillPayForm was not returned by a get request ", form);
        //final ModelAndView mav = controller.handleRequest(request, response);
        assertReferenceData(mav);
    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        final BillPayForm form = getBillPayForm();
        //final BindException errors = new BindException(form, Constants.BILLPAYFORM);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        final ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertEquals("billpayConfirm", mav.getViewName());
        assertEquals("payee name", getModelValue(mav, "payeeName"));
        assertEquals(new BigDecimal("100.0"), getModelValue(mav, "amount"));
        assertEquals(12345, getModelValue(mav, "fromAccountId"));
    }

    @Test
    @Transactional
    @Rollback
    public void testValidate() throws Exception {
        BillPayForm form = getBillPayForm();
        form.getPayee().setPhoneNumber(null);
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "payee.phoneNumber", "error.phone.number.required");

        form = getBillPayForm();
        form.setAmount(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "amount", "error.amount.empty");

        form = getBillPayForm();
        form.getPayee().setAccountNumber(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "payee.accountNumber", "error.account.number.required");

        form = getBillPayForm();
        form.setVerifyAccount(200);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "verifyAccount", "error.account.number.mismatch");
    }
}

//"name", "error.payee.name.required"
//"phoneNumber", "error.phone.number.required"
//"accountNumber", "error.account.number.required"
//"amount", "error.amount.empty"
//"verifyAccount", "error.account.number.mismatch"
