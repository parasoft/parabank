package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import org.junit.*;
import org.springframework.mock.web.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.form.*;

/**
 * @req PAR-29
 * @req PAR-41
 */
public class UpdateCustomerControllerTest extends AbstractCustomerControllerTest<UpdateCustomerController> {
    @Override
    protected CustomerForm createCustomerForm() throws Exception {
        //return (CustomerForm) controller.formBackingObject(request);
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        return (CustomerForm) mav.getModel().get(getFormName());
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/updateprofile.htm");
        setFormName(Constants.CUSTOMERFORMUPDATE);
        registerSession(request);

    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        final CustomerForm form = getCustomerForm();
        //final BindException errors = new BindException(form, Constants.CUSTOMERFORMUPDATE);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        final ModelAndView mav = processPostRequest(form, request, new MockHttpServletResponse());

        assertEquals("updateprofileConfirm", mav.getViewName());
        final Customer customer = (Customer) mav.getModel().get("customer");
        assertEquals(12212, customer.getId());
    }
}
