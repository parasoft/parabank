package com.parasoft.parabank.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.AdminParameters;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;

/**
 * Controller for creating a new bank account
 */
@Controller("secure_openaccount")
@SessionAttributes(Constants.OPENACCOUNTFORM)
@RequestMapping("/openaccount.htm")
public class OpenAccountController extends AbstractBankController {
    //private static final Logger log = LoggerFactory.getLogger(OpenAccountController.class);

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAccountForm(final Model model) throws Exception {
        final ModelAndView mav = super.prepForm(model);
        return mav;
    }

    @ModelAttribute("types")
    public List<AccountType> getAccountTypes() {
        final List<AccountType> types = new ArrayList<>();
        for (final AccountType type : AccountType.values()) {
            if (!type.isInternal()) {
                types.add(type);
            }
        }
        return types;
    }

    @ModelAttribute("minimumBalance")
    public String getMinimumBalance() {
        return adminManager.getParameter(AdminParameters.MINIMUM_BALANCE);
    }

    @ModelAttribute("customerId")
    public int getCustomerId(@SessionParam(Constants.USERSESSION) final UserSession userSession) {
        return userSession.getCustomer().getId();
    }


    //    @RequestMapping(method = RequestMethod.POST)
    //    public ModelAndView onSubmit(@ModelAttribute(Constants.OPENACCOUNTFORM) final OpenAccountForm openAccountForm,
    //        final BindingResult errors, @SessionParam(Constants.USERSESSION) final UserSession userSession)
    //                throws Exception {
    //        if (errors != null && errors.hasErrors()) {
    //            return new ModelAndView(getFormView(), errors.getModel());
    //        }
    //
    //        //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //        //final UserSession userSession = (UserSession) session.getAttribute(Constants.USERSESSION);
    //        //        if (Util.isEmpty(userSession)) {
    //        //            final String message = "No 'userSession' found on the current Http session";
    //        //            log.error(message);
    //        //            throw new IllegalStateException(message);
    //        //        }
    //
    //        Account newAccount;
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (!Util.isEmpty(accessMode) && !accessMode.equalsIgnoreCase("jdbc")) {
    //            newAccount = accessModeController.createAccount(userSession.getCustomer().getId(),
    //                openAccountForm.getType().ordinal(), openAccountForm.getFromAccountId());
    //        } else {
    //            newAccount = new Account();
    //            newAccount.setCustomerId(userSession.getCustomer().getId());
    //            newAccount.setType(openAccountForm.getType());
    //            newAccount.setBalance(BigDecimal.ZERO);
    //            bankManager.createAccount(newAccount, openAccountForm.getFromAccountId());
    //            if (Util.isEmpty(accessMode)) {
    //                log.warn("Using regular JDBC connection by default. accessMode not set.");
    //            } else {
    //                log.info("Using regular JDBC connection");
    //            }
    //        }
    //
    //        return new ModelAndView("openaccountConfirm", "account", newAccount);
    //    }

    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classOpenAccountForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.OPENACCOUNTFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.OPENACCOUNT)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

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
    //        model.put("minimumBalance", adminManager.getParameter(AdminParameters.MINIMUM_BALANCE));
    //        model.put("accounts", accountIds);
    //        final List<AccountType> types = new ArrayList<AccountType>();
    //        for (final AccountType type : AccountType.values()) {
    //            if (!type.isInternal()) {
    //                types.add(type);
    //            }
    //        }
    //        model.put("types", types);
    //
    //        return model;
    //    }
    //    @Override
    //    protected ModelAndView onSubmit(final HttpServletRequest request, final HttpServletResponse response,
    //        final Object command, final BindException errors) throws Exception {
    //
    //        final Log log = LogFactory.getLog(OpenAccountController.class);
    //        final OpenAccountForm openAccountForm = (OpenAccountForm) command;
    //
    //        final UserSession userSession = (UserSession) WebUtils.getRequiredSessionAttribute(request, Constants.USERSESSION);
    //
    //        Account newAccount;
    //
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            newAccount = accessModeController.createAccount(userSession.getCustomer().getId(),
    //                openAccountForm.getType().ordinal(), openAccountForm.getFromAccountId());
    //        } else {
    //            newAccount = new Account();
    //            newAccount.setCustomerId(userSession.getCustomer().getId());
    //            newAccount.setType(openAccountForm.getType());
    //            newAccount.setBalance(BigDecimal.ZERO);
    //            bankManager.createAccount(newAccount, openAccountForm.getFromAccountId());
    //            log.warn("Using regular JDBC connection");
    //        }
    //
    //        return new ModelAndView("openaccountConfirm", "account", newAccount);
    //    }

}
