package com.parasoft.parabank.web.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.*;
import com.parasoft.parabank.web.form.*;

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

        final List<Integer> accountIds = new ArrayList<Integer>();
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
