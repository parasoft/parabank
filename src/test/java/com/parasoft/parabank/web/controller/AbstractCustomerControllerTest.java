package com.parasoft.parabank.web.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.validator.AddressValidator;
import com.parasoft.parabank.domain.validator.CustomerValidator;
import com.parasoft.parabank.web.form.CustomerForm;

public abstract class AbstractCustomerControllerTest<T extends AbstractValidatingBankController>
        extends AbstractValidatingBankControllerTest<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractCustomerControllerTest.class);

    protected abstract CustomerForm createCustomerForm() throws Exception;

    protected CustomerForm getCustomerForm() throws Exception {
        final CustomerForm form = createCustomerForm();
        form.getCustomer().setId(12212);
        form.getCustomer().setFirstName("first name");
        form.getCustomer().setLastName("last name");
        final Address address = new Address();
        address.setStreet("customer street");
        address.setCity("customer city");
        address.setState("customer state");
        address.setZipCode("customer zipcode");
        form.getCustomer().setAddress(address);
        form.getCustomer().setPhoneNumber("phone number");
        form.getCustomer().setSsn("customer ssn");
        form.getCustomer().setUsername("customer username");
        form.getCustomer().setPassword("customer password");
        form.setRepeatedPassword("customer password");
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        final CustomerValidator validator = new CustomerValidator();
        validator.setAddressValidator(new AddressValidator());
        controller.setValidator(validator);
    }

    @Test
    public void testCustomerFormValidation() throws Exception {
        CustomerForm form = getCustomerForm();
        form.getCustomer().setFirstName(null);
        ModelAndView mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());

        //assertError(form, "customer.firstName");

        //response = new MockHttpServletResponse();
        //request = new MockHttpServletRequest();
        form = getCustomerForm();
        form.getCustomer().setLastName(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());
        //assertError(form, "customer.lastName");

        form = getCustomerForm();
        form.getCustomer().getAddress().setStreet(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());
        //assertError(form, "customer.address.street");

        form = getCustomerForm();
        form.getCustomer().getAddress().setCity(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());
        //assertError(form, "customer.address.city");

        form = getCustomerForm();
        form.getCustomer().getAddress().setState(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());
        //assertError(form, "customer.address.state");

        form = getCustomerForm();
        form.getCustomer().getAddress().setZipCode(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        log.info("Errors: {}", mav.getModelMap());
        //assertError(form, "customer.address.zipCode");
    }
}
