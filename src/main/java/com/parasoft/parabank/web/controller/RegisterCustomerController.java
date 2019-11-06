package com.parasoft.parabank.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.form.CustomerForm;

/**
 * Controller for creating a new bank customer
 */
@Controller("/register.htm")
@SessionAttributes(Constants.CUSTOMERFORM)
@RequestMapping("/register.htm")
public class RegisterCustomerController extends AbstractValidatingBankController {
    private static final Logger log = LoggerFactory.getLogger(RegisterCustomerController.class);

    //    @Resource(name = "customerValidator")
    //    private Validator customerValidator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getCustomerForm(final Model model) throws Exception {
        final ModelAndView mv = super.prepForm(model);
        return mv;
    }

    //    /**
    //     * <DL>
    //     * <DT>Description:</DT>
    //     * <DD>Getter for the customerValidator property</DD>
    //     * <DT>Date:</DT>
    //     * <DD>Oct 15, 2015</DD>
    //     * </DL>
    //     *
    //     * @return the value of customerValidator field
    //     */
    //    public Validator getCustomerValidator() {
    //        return customerValidator;
    //    }

    //    @Override
    //    @InitBinder
    //    protected void initBinder(final WebDataBinder binder) {
    //        super.initBinder(binder);
    //        binder.addValidators(getCustomerValidator());
    //    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Valid @ModelAttribute(Constants.CUSTOMERFORM) final CustomerForm customerForm,
        final BindingResult errors, final HttpSession session) throws Exception {
        //        final CustomerForm customerForm = (CustomerForm) command;
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        try {
            bankManager.createCustomer(customerForm.getCustomer());
        } catch (final DataIntegrityViolationException ex) {
            log.warn("Username " + customerForm.getCustomer().getUsername() + " already exists in database");
            errors.rejectValue("customer.username", "error.username.already.exists");
            final ModelAndView mav = new ModelAndView(getFormView(), errors.getModel());
            return mav;
            //return showForm(request, response, errors);
        }

        final UserSession userSession = new UserSession(bankManager.getCustomer(customerForm.getCustomer().getId()));
        session.setAttribute(Constants.USERSESSION, userSession);

        return new ModelAndView("registerConfirm", "customer", customerForm.getCustomer());
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classCustomerForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.CUSTOMERFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    //    /**
    //     * <DL>
    //     * <DT>Description:</DT>
    //     * <DD>Setter for the customerValidator property</DD>
    //     * <DT>Date:</DT>
    //     * <DD>Oct 15, 2015</DD>
    //     * </DL>
    //     *
    //     * @param aCustomerValidator
    //     *            new value for the customerValidator property
    //     */
    //    public void setCustomerValidator(final Validator aCustomerValidator) {
    //        customerValidator = aCustomerValidator;
    //    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.REGISTER)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "customerFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;

    }

    //    @Override
    //    protected Object formBackingObject(HttpServletRequest request)
    //            throws Exception {
    //        return new CustomerForm();
    //    }
    //
    //    @Override
    //    protected void onBindAndValidate(HttpServletRequest request,
    //            Object command, BindException errors) throws Exception {
    //        CustomerForm customerForm = (CustomerForm)command;
    //        Customer customer = customerForm.getCustomer();
    //
    //        try {
    //            errors.pushNestedPath("customer");
    //            getValidator().validate(customer, errors);
    //        } finally {
    //            errors.popNestedPath();
    //        }
    //
    //        if (customerForm.getRepeatedPassword() == null || customerForm.getRepeatedPassword().length() <= 0) {
    //            errors.rejectValue("repeatedPassword", "error.password.confirmation.required");
    //        } else if (customerForm.getCustomer().getPassword() != null && customerForm.getCustomer().getPassword().length() > 0 &&
    //                !customer.getPassword().equals(customerForm.getRepeatedPassword())) {
    //            errors.rejectValue("repeatedPassword", "error.password.mismatch");
    //        }
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(HttpServletRequest request,
    //            HttpServletResponse response, Object command, BindException errors)
    //            throws Exception {
    //        CustomerForm customerForm = (CustomerForm)command;
    //        try {
    //            bankManager.createCustomer(customerForm.getCustomer());
    //        } catch (DataIntegrityViolationException ex) {
    //            log.warn("Username " + customerForm.getCustomer().getUsername() + " already exists in database");
    //            errors.rejectValue("customer.username", "error.username.already.exists");
    //            return showForm(request, response, errors);
    //        }
    //
    //        UserSession userSession = new UserSession(bankManager.getCustomer(customerForm.getCustomer().getId()));
    //        request.getSession().setAttribute(Constants.USERSESSION, userSession);
    //
    //        return new ModelAndView("registerConfirm", "customer", customerForm.getCustomer());
    //    }
}
