package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.form.CustomerForm;

/**
 * @req PAR-3
 * @req PAR-38
 * @req PAR-29
 * @req PAR-10
 *
 */
public class RegisterCustomerControllerTest extends AbstractCustomerControllerTest<RegisterCustomerController> {
    private static final Logger log = LoggerFactory.getLogger(RegisterCustomerControllerTest.class);
    //    @Override
    //    protected CustomerForm createCustomerForm() throws Exception {
    //        return (CustomerForm) controller.formBackingObject(request);
    //    }

    @Override
    protected CustomerForm createCustomerForm() throws Exception {
        final ModelAndView mav = super.processGetRequest(request, response);
        final CustomerForm form = (CustomerForm) mav.getModel().get(Constants.CUSTOMERFORM);
        return form;
        //return null;
    }

    /** {@inheritDoc} */
    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/register.htm");
        setFormName(Constants.CUSTOMERFORM);
    }

    @Override
    @Test
    public void testCustomerFormValidation() throws Exception {
        super.testCustomerFormValidation();
        CustomerForm form = getCustomerForm();
        form.setRepeatedPassword(null);
        ModelAndView mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertError(mav, "repeatedPassword", "error.password.confirmation.required");
        //assertError(form, "repeatedPassword");

        form = getCustomerForm();
        form.setRepeatedPassword("password");
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertError(mav, "repeatedPassword", "error.password.mismatch");
        //assertError(form, "repeatedPassword");
    }

    @Test
    public void testDuplicateUsername() throws Exception {
        final CustomerForm form = getCustomerForm();
        form.getCustomer().setUsername("john");
        final ModelAndView mav = super.processPostRequest(form, request, new MockHttpServletResponse());
        log.info("testDuplicateUsername Resilts: {}", mav);
        //final BindException errors = new BindException(form, Constants.CUSTOMERFORM);
        //controller.onSubmit(form, errors, request.getSession(true));
        //assertEquals(1, errors.getErrorCount());
        //assertNotNull(errors.getFieldError("customer.username"));
    }

    @Test
    public void testOnSubmit() throws Exception {
        final CustomerForm form = getCustomerForm();
        //final BindException errors = new BindException(form, Constants.CUSTOMERFORM);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        final ModelAndView mav = processPostRequest(form, request, new MockHttpServletResponse());
        assertEquals("registerConfirm", mav.getViewName());
        final Customer customer = (Customer) mav.getModel().get("customer");
        assertEquals(12434, customer.getId());

        final UserSession session = (UserSession) request.getSession().getAttribute(Constants.USERSESSION);
        assertNotNull(session);
        assertEquals(customer, session.getCustomer());
    }
}
