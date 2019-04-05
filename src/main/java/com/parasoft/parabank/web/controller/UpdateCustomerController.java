package com.parasoft.parabank.web.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

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
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.service.ParaBankServiceException;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.form.CustomerForm;

/**
 * Controller for updating customer information
 */
@Controller("secure_updateprofile")
@SessionAttributes(Constants.CUSTOMERFORMUPDATE)
@RequestMapping("/updateprofile.htm")
public class UpdateCustomerController extends AbstractValidatingBankController {
    private static final Logger log = LoggerFactory.getLogger(UpdateCustomerController.class);

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    public Customer getCustomer(final int custId) throws ParaBankServiceException, IOException, JAXBException {
        Customer cu;
        cu = accessModeController.doGetCustomer(custId);
        return cu;
    }

    //    @Override
    //    protected Object formBackingObject(final HttpServletRequest request) throws Exception {
    //
    //        CustomerForm cf = null;
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //        cf = new CustomerForm(bankManager.getCustomer(userSession.getCustomer().getId()));
    //        return cf;
    //    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getLookupForm(@SessionParam(Constants.USERSESSION) final UserSession userSession,
        final Model model) throws Exception {
        final CustomerForm form = new CustomerForm(bankManager.getCustomer(userSession.getCustomer().getId()));
        form.setRepeatedPassword(form.getCustomer().getPassword());
        final ModelAndView mv = super.prepForm(model, form);
        mv.addObject("customerId", userSession.getCustomer().getId());
        mv.addObject("username", userSession.getCustomer().getUsername());
        mv.addObject("password", userSession.getCustomer().getPassword());
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(
        @Validated @ModelAttribute(Constants.CUSTOMERFORMUPDATE) final CustomerForm customerForm,
        final BindingResult errors, final HttpSession session) throws Exception {
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        //        final CustomerForm customerForm = (CustomerForm) command;

        String accessMode = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        log.info("Updating Customer Contact Information");
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            accessModeController.updateCustomer(customerForm.getCustomer());
            final UserSession userSession = new UserSession(getCustomer(customerForm.getCustomer().getId()));
            session.setAttribute(Constants.USERSESSION, userSession);
        }

        else {
            bankManager.updateCustomer(customerForm.getCustomer());
            final UserSession userSession =
                new UserSession(bankManager.getCustomer(customerForm.getCustomer().getId()));
            session.setAttribute(Constants.USERSESSION, userSession);
        }

        return new ModelAndView("updateprofileConfirm", "customer", customerForm.getCustomer());
    }

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classCustomerForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.CUSTOMERFORMUPDATE)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.UPDATEPROFILE)
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
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        final CustomerForm customerForm = (CustomerForm) command;
    //        final Customer customer = customerForm.getCustomer();
    //
    //        try {
    //            errors.pushNestedPath("customer");
    //            getValidator().validate(customer, errors);
    //        } finally {
    //            errors.popNestedPath();
    //        }
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
    //        final Object command, final BindException errors) throws Exception {
    //
    //        final CustomerForm customerForm = (CustomerForm) command;
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            accessModeController.updateCustomer(customerForm.getCustomer());
    //            final UserSession userSession = new UserSession(getCustomer(customerForm.getCustomer().getId()));
    //            request.getSession().setAttribute(Constants.USERSESSION, userSession);
    //        }
    //
    //        else {
    //            bankManager.updateCustomer(customerForm.getCustomer());
    //            final UserSession userSession =
    //                new UserSession(bankManager.getCustomer(customerForm.getCustomer().getId()));
    //            request.getSession().setAttribute(Constants.USERSESSION, userSession);
    //        }
    //
    //        return new ModelAndView("updateprofileConfirm", "customer", customerForm.getCustomer());
    //    }

}
