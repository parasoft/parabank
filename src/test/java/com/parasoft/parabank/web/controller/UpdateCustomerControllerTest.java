package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.CustomerForm;

/**
 * @req PAR-29
 * @req PAR-41
 * @req PAR-4
 * @req PAR-31
 *
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
