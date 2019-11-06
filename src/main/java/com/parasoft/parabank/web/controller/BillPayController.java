package com.parasoft.parabank.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.logic.BankManager;
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
public class BillPayController {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired
    @Qualifier("bankManager")
    private BankManager bankManager;

    @Autowired
    @Qualifier("billPayFormValidator")
    private Validator validator;

    @ModelAttribute("accounts")
    public List<Integer> getAccountIds(@SessionParam(Constants.USERSESSION) final UserSession userSession)
            throws Exception {

        final Customer customer = userSession.getCustomer();
        final List<Account> accounts = bankManager.getAccountsForCustomer(customer);

        final List<Integer> accountIds = new ArrayList<>();
        for (final Account account : accounts) {
            accountIds.add(account.getId());
        }
        return accountIds;
    }

    @GetMapping
    public ModelAndView getForm(final Model model) throws Exception {
        model.addAttribute(Constants.BILLPAYFORM, new BillPayForm());
        return new ModelAndView(Constants.BILLPAY, model.asMap());
    }

    @PostMapping
    public ModelAndView onSubmit(@Validated @ModelAttribute(Constants.BILLPAYFORM) final BillPayForm billPayForm,
        final BindingResult errors, final Model model, final java.util.Locale locale) throws Exception {
        if (errors.hasErrors()) {
            return new ModelAndView(Constants.BILLPAY, errors.getModel());
        }

        bankManager.withdraw(billPayForm.getFromAccountId(), billPayForm.getAmount(),
            messageSource.getMessage("bill.payment.to", new Object[] { billPayForm.getPayee().getName() }, locale));

        model.addAttribute("payeeName", billPayForm.getPayee().getName());
        model.addAttribute("amount", billPayForm.getAmount());
        model.addAttribute("fromAccountId", billPayForm.getFromAccountId());

        return new ModelAndView("billpayConfirm", "model", model);
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(validator);
    }
}
