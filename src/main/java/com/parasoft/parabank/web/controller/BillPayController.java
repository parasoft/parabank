package com.parasoft.parabank.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
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

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.form.BillPayForm;

/**
 * Controller for online bill pay
 */
@Controller("secure_billpay")
@SessionAttributes(Constants.BILLPAYFORM)
@RequestMapping("/billpay.htm")
public class BillPayController extends AbstractValidatingBankController {

    @Resource(name = "messageSource")
    private MessageSource messageSource;

    //    @ModelAttribute("accounts")
    //    public List<Integer> getAccountIds(final HttpServletRequest request) {
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    @ModelAttribute("accounts")
    public List<Integer> getAccountIds(@SessionParam(Constants.USERSESSION) final UserSession userSession)
            throws Exception {

        final Customer customer = userSession.getCustomer();
        final List<Account> accounts = bankManager.getAccountsForCustomer(customer);

        final List<Integer> accountIds = new ArrayList<Integer>();
        for (final Account account : accounts) {
            accountIds.add(account.getId());
        }
        return accountIds;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getForm(final Model model) throws Exception {
        final ModelAndView mv = super.prepForm(model);
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Validated @ModelAttribute(Constants.BILLPAYFORM) final BillPayForm billPayForm,
        final BindingResult errors, final Model model, final java.util.Locale locale) throws Exception {
        // final BillPayForm billPayForm = (BillPayForm) command;
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        bankManager.withdraw(billPayForm.getFromAccountId(), billPayForm.getAmount(),
            messageSource.getMessage("bill.payment.to", new Object[] { billPayForm.getPayee().getName() }, locale));

        // final Map<String, Object> model = new HashMap<String, Object>();
        model.addAttribute("payeeName", billPayForm.getPayee().getName());
        model.addAttribute("amount", billPayForm.getAmount());
        model.addAttribute("fromAccountId", billPayForm.getFromAccountId());

        return new ModelAndView("billpayConfirm", "model", model);
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // TODO Add AccessModeController implementation

    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classBillPayForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.BILLPAYFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.BILLPAY)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    @Resource(name = "billPayFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }

    //    @Override
    //    protected Object formBackingObject(final HttpServletRequest request) throws Exception {
    //        return new BillPayForm();
    //    }
    //
    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        final BillPayForm billPayForm = (BillPayForm) command;
    //        final Payee payee = billPayForm.getPayee();
    //
    //        try {
    //            errors.pushNestedPath("payee");
    //            getValidator().validate(payee, errors);
    //        } finally {
    //            errors.popNestedPath();
    //        }
    //
    //        ValidationUtils.rejectIfEmpty(errors, "amount", "error.amount.empty");
    //
    //        if (payee.getAccountNumber() != null && !payee.getAccountNumber().equals(billPayForm.getVerifyAccount())) {
    //            errors.rejectValue("verifyAccount", "error.account.number.mismatch");
    //        }
    //    } // @Override
    //
    //    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
    //        final Object command, final BindException errors) throws Exception {
    //        final BillPayForm billPayForm = (BillPayForm) command;
    //
    //        bankManager.withdraw(billPayForm.getFromAccountId(), billPayForm.getAmount(), messageSource
    //            .getMessage("bill.payment.to", new Object[] { billPayForm.getPayee().getName() }, request.getLocale()));
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("payeeName", billPayForm.getPayee().getName());
    //        model.put("amount", billPayForm.getAmount());
    //        model.put("fromAccountId", billPayForm.getFromAccountId());
    //
    //        return new ModelAndView("billpayConfirm", "model", model);
    //    }
    //
    //    @Override
    //    protected Map<String, Object> referenceData(final HttpServletRequest request) throws Exception {
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //
    //        final Customer customer = userSession.getCustomer();
    //        final List<Account> accounts = bankManager.getAccountsForCustomer(customer);
    //
    //        final List<Integer> accountIds = new ArrayList<Integer>();
    //        for (final Account account : accounts) {
    //            accountIds.add(account.getId());
    //        }
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("accounts", accountIds);
    //
    //        return model;
    //    }
}
