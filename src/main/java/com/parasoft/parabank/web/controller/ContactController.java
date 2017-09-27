package com.parasoft.parabank.web.controller;

import java.util.*;

import javax.annotation.*;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.form.*;

/**
 * Controller for collecting customer support messages (currently ignores the message)
 */
@Controller("/contact.htm")
@SessionAttributes(Constants.CONTACTFORM)
@RequestMapping("/contact.htm")
public class ContactController extends AbstractValidatingBankController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getContactForm(final Model model) throws Exception {
        final ModelAndView mv = super.prepForm(model);
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(
        @Validated @ModelAttribute(Constants.CONTACTFORM) final ContactForm customerServiceForm,
        final BindingResult errors) throws Exception {
        // final ContactForm customerServiceForm = (ContactForm) command;
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("name", customerServiceForm.getName());

        return new ModelAndView("contactConfirm", "model", model);
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classContactForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.CONTACTFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.CONTACT)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    @Override
    @Resource(name = "contactFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }

    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        ValidationUtils.rejectIfEmpty(errors, "name", "error.name.empty");
    //        ValidationUtils.rejectIfEmpty(errors, "email", "error.email.empty");
    //        ValidationUtils.rejectIfEmpty(errors, "phone", "error.phone.empty");
    //        ValidationUtils.rejectIfEmpty(errors, "message", "error.message.empty");
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(final Object command) throws Exception {
    //        final ContactForm customerServiceForm = (ContactForm) command;
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("name", customerServiceForm.getName());
    //
    //        return new ModelAndView("contactConfirm", "model", model);
    //    }
}
