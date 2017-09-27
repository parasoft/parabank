package com.parasoft.parabank.web.controller;

import java.util.*;

import javax.annotation.*;

import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;
import com.parasoft.parabank.web.*;
import com.parasoft.parabank.web.form.*;

/**
 * Controller for applying for a loan
 */
@Controller("secure_requestloan")
@SessionAttributes(Constants.REQUESTLOANFORM)
@RequestMapping("/requestloan.htm")
public class RequestLoanController extends AbstractValidatingBankController {

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @ModelAttribute("accounts")
    public List<Integer> getAccountIds(@SessionParam(Constants.USERSESSION) final UserSession userSession) {
        //final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);

        final Customer customer = userSession.getCustomer();
        final List<Account> accounts = bankManager.getAccountsForCustomer(customer);

        final List<Integer> accountIds = new ArrayList<Integer>();
        for (final Account account : accounts) {
            accountIds.add(account.getId());
        }
        return accountIds;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getRequestLoanForm(final Model model) throws Exception {
        final ModelAndView mav = super.prepForm(model);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Validated @ModelAttribute(Constants.REQUESTLOANFORM) final RequestLoanForm requestLoanForm,
        final BindingResult errors, @SessionParam(Constants.USERSESSION) final UserSession userSession)
                throws Exception {

        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }
        LoanResponse loanResponse = null;
        //final RequestLoanForm requestLoanForm = (RequestLoanForm) command;

        //final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
        final Customer customer = userSession.getCustomer();

        String accessMode = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }

        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            loanResponse = accessModeController.doRequestLoan(customer.getId(), requestLoanForm.getAmount(),
                requestLoanForm.getDownPayment(), requestLoanForm.getFromAccountId());
        }

        else {
            loanResponse = bankManager.requestLoan(customer.getId(), requestLoanForm.getAmount(),
                requestLoanForm.getDownPayment(), requestLoanForm.getFromAccountId());

        }

        return new ModelAndView("requestloanConfirm", "loanResponse", loanResponse);
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
    @Resource(name = "classRequestLoanForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.REQUESTLOANFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.REQUESTLOAN)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    @Override
    @Resource(name = "requestLoanFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }

    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        ValidationUtils.rejectIfEmpty(errors, "amount", "error.loan.amount.empty");
    //        ValidationUtils.rejectIfEmpty(errors, "downPayment", "error.down.payment.empty");
    //        super.onBindAndValidate(request, command, errors);
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
    //        final Object command, final BindException errors) throws Exception {
    //        LoanResponse loanResponse = null;
    //        final RequestLoanForm requestLoanForm = (RequestLoanForm) command;
    //
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //        final Customer customer = userSession.getCustomer();
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            loanResponse = accessModeController.doRequestLoan(customer.getId(), requestLoanForm.getAmount(),
    //                requestLoanForm.getDownPayment(), requestLoanForm.getFromAccountId());
    //        }
    //
    //        else {
    //            loanResponse = bankManager.requestLoan(customer.getId(), requestLoanForm.getAmount(),
    //                requestLoanForm.getDownPayment(), requestLoanForm.getFromAccountId());
    //
    //        }
    //
    //        return new ModelAndView("requestloanConfirm", "loanResponse", loanResponse);
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
