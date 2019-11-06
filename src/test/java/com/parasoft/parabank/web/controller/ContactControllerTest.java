package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.ContactForm;

/**
 * @req PAR-12
 * @req PAR-15
 * @req PAR-16
 * @req PAR-41
 *
 */
public class ContactControllerTest extends AbstractValidatingBankControllerTest<ContactController> {
    private ContactForm getContactForm() {
        final ContactForm form = new ContactForm();
        form.setName("customer name");
        form.setEmail("customer email");
        form.setPhone("customer phone");
        form.setMessage("customer message");
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/contact.htm");
        setFormName(Constants.CONTACTFORM);
        registerSession(request);
    }

    @Test
    public void testGetContactForm() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final ContactForm form = (ContactForm) mav.getModel().get(getFormName());
        assertNotNull("ContactForm was not returned by a get request ", form);
    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        final ContactForm form = getContactForm();
        final ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //final ModelAndView mav = controller.onSubmit(getContactForm());
        assertEquals("contactConfirm", mav.getViewName());
        assertEquals("customer name", getModelValue(mav, "name"));
    }

    @Test
    @Transactional
    @Rollback
    public void testValidate() throws Exception {
        ContactForm form = new ContactForm();
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //TODO: inject the values from spring
        getErrorMap().put("name", "error.name.empty");
        getErrorMap().put("email", "error.email.empty");
        getErrorMap().put("phone", "error.phone.empty");
        getErrorMap().put("message", "error.message.empty");
        assertError(mav, getErrorMap());

        form = getContactForm();
        form.setName(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "name", getErrorMap().get("name"));

        form = getContactForm();
        form.setEmail(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "email", getErrorMap().get("email"));

        form = getContactForm();
        form.setPhone(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "phone", getErrorMap().get("phone"));

        form = getContactForm();
        form.setMessage(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "message", getErrorMap().get("message"));
    }
}
//"name", "error.name.empty"
//"email", "error.email.empty"
//"phone", "error.phone.empty"
//"message", "error.message.empty"
