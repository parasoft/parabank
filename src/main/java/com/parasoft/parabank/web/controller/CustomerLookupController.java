package com.parasoft.parabank.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.ViewUtil;
import com.parasoft.parabank.web.form.LookupForm;

/**
 * Controller for retrieving lost customer signin info
 */
@Controller("/lookup.htm")
@SessionAttributes(Constants.LOOKUPFORM)
@RequestMapping("/lookup.htm")
public class CustomerLookupController extends AbstractValidatingBankController {
    private static final Logger log = LoggerFactory.getLogger(CustomerLookupController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getLookupForm(final Model model) throws Exception {
        final ModelAndView mv = super.prepForm(model);
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Validated @ModelAttribute(Constants.LOOKUPFORM) final LookupForm lookupForm,
        final BindingResult errors, final HttpSession session) throws Exception {
        // final LookupForm lookupForm = (LookupForm) command;
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        final String ssn = lookupForm.getSsn();
        final Customer customer = bankManager.getCustomer(ssn);
        if (customer == null) {
            log.error("Invalid SSN = " + ssn);
            return ViewUtil.createErrorView("error.invalid.ssn");
        }

        final UserSession userSession = new UserSession(customer);
        session.setAttribute(Constants.USERSESSION, userSession);

        return new ModelAndView("lookupConfirm", "customer", customer);
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classLookupForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.LOOKUPFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.LOOKUP)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "lookupFormValidator")
    public void setValidator(final Validator aValidator) {
        super.validator = aValidator;
    }

    //    @Override
    //    protected Object formBackingObject(final HttpServletRequest request) throws Exception {
    //        return new LookupForm();
    //    }
    //
    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        final LookupForm lookupForm = (LookupForm) command;
    //        final Address address = lookupForm.getAddress();
    //
    //        try {
    //            errors.pushNestedPath("address");
    //            getValidator().validate(address, errors);
    //        } finally {
    //            errors.popNestedPath();
    //        }
    //
    //        ValidationUtils.rejectIfEmpty(errors, "firstName", "error.first.name.required");
    //        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.last.name.required");
    //        ValidationUtils.rejectIfEmpty(errors, "ssn", "error.ssn.required");
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
    //        final Object command, final BindException errors) throws Exception {
    //        final LookupForm lookupForm = (LookupForm) command;
    //
    //        final String ssn = lookupForm.getSsn();
    //        final Customer customer = bankManager.getCustomer(ssn);
    //        if (customer == null) {
    //            log.error("Invalid SSN = " + ssn);
    //            return ViewUtil.createErrorView("error.invalid.ssn");
    //        }
    //
    //        final UserSession userSession = new UserSession(customer);
    //        request.getSession().setAttribute(Constants.USERSESSION, userSession);
    //
    //        return new ModelAndView("lookupConfirm", "customer", customer);
    //    }
}
