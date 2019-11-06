package com.parasoft.parabank.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;

/**
 * Controller for transferring funds between accounts
 */
@Controller("secure_transfer")
@SessionAttributes(Constants.TRANSFERFORM)
@RequestMapping("/transfer.htm")
public class TransferController extends AbstractValidatingBankController {

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @ModelAttribute("accounts")
    public List<Integer> getAccountIds(@SessionParam(Constants.USERSESSION) final UserSession userSession) {
        //final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);

        final Customer customer = userSession.getCustomer();
        final List<Account> accounts = bankManager.getAccountsForCustomer(customer);

        final List<Integer> accountIds = new ArrayList<>();
        for (final Account account : accounts) {
            accountIds.add(account.getId());
        }
        return accountIds;
    }

    @ModelAttribute("customerId")
    public int getCustomerId(@SessionParam(Constants.USERSESSION) final UserSession userSession) {
        return userSession.getCustomer().getId();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getTransferForm(final Model model) throws Exception {
        final ModelAndView mav = super.prepForm(model);
        return mav;
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
    @Resource(name = "classTransferForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.TRANSFERFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.TRANSFER)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    @Override
    @Resource(name = "transferFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }

    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        ValidationUtils.rejectIfEmpty(errors, "amount", "error.amount.empty");
    //        super.onBindAndValidate(request, command, errors);
    //    }
    //
    //    @Override
    //    protected ModelAndView onSubmit(final Object command) throws Exception {
    //        final TransferForm transferForm = (TransferForm) command;
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            accessModeController.doTransfer(transferForm.getFromAccountId(), transferForm.getToAccountId(),
    //                transferForm.getAmount());
    //        }
    //
    //        else {
    //            bankManager.transfer(transferForm.getFromAccountId(), transferForm.getToAccountId(),
    //                transferForm.getAmount());
    //        }
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("amount", transferForm.getAmount());
    //        model.put("fromAccountId", transferForm.getFromAccountId());
    //        model.put("toAccountId", transferForm.getToAccountId());
    //
    //        return new ModelAndView("transferConfirm", "model", model);
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
