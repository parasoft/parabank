package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.LookupForm;

/**
 * @req PAR-28
 * @req PAR-29
 * @req PAR-30
 * @req PAR-33
 *
 */
public class CustomerLookupControllerTest extends AbstractValidatingBankControllerTest<CustomerLookupController> {
    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add getLookupForm description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return
     * @throws Exception
     */
    private LookupForm getLookupForm() throws Exception {
        final ModelAndView mav =
            processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        final LookupForm form = (LookupForm) mav.getModel().get(getFormName());
        assertNotNull("LookupForm was not returned by a get request ", form);
        form.setFirstName("first name");
        form.setLastName("last name");
        final Address address = new Address();
        address.setStreet("customer street");
        address.setCity("customer city");
        address.setState("customer state");
        address.setZipCode("customer zip code");
        form.setAddress(address);
        form.setSsn("622-11-9999");
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/lookup.htm");
        setFormName(Constants.LOOKUPFORM);
        registerSession(request);
        //controller.setValidator(new AddressValidator());

    }

    @Test
    @Transactional
    @Rollback
    public void testHandlePostRequest() throws Exception {
        LookupForm form = getLookupForm();
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //        BindException errors = new BindException(form, Constants.LOOKUPFORM);
        //        ModelAndView mav = controller.onSubmit(request, response, form, errors);
        assertEquals("lookupConfirm", mav.getViewName());
        final Customer customer = (Customer) mav.getModel().get("customer");
        assertEquals(12212, customer.getId());

        form = getLookupForm();
        form.setSsn("invalid");
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertEquals("error", mav.getViewName());
        assertEquals("error.invalid.ssn", getModelValue(mav, "message"));

        //        errors = new BindException(form, Constants.LOOKUPFORM);
        //        mav = controller.onSubmit(request, response, form, errors);
        //        assertEquals("error.invalid.ssn", getModelValue(mav, "message"));
    }

    @Test
    @Transactional
    @Rollback
    public void testValidate() throws Exception {
        LookupForm form = getLookupForm();
        form.setFirstName(null);
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "firstName", "error.first.name.required");

        form = getLookupForm();
        form.setLastName(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "lastName", "error.last.name.required");

        form = getLookupForm();
        form.setSsn(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "ssn", "error.ssn.required");
    }
}
//"firstName", "error.first.name.required"
//"lastName", "error.last.name.required"
//"ssn", "error.ssn.required"
