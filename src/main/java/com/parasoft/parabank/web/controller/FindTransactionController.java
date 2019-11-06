package com.parasoft.parabank.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.SessionParam;
import com.parasoft.parabank.web.UserSession;
import com.parasoft.parabank.web.form.FindTransactionForm;

/**
 * Controller for searching transactions
 */
@Controller("secure_findtrans")
@SessionAttributes(Constants.FINDTRANSACTIONFORM)
@RequestMapping("/findtrans.htm")
public class FindTransactionController extends AbstractValidatingBankController {
    private static final Logger log = LoggerFactory.getLogger(FindTransactionController.class);

    //private List<Transaction> transactions = null;

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add getAccountIds description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param request
     * @return
     */
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

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add getFindTransactionForm description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param errors
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getFindTransactionForm(final Model model) throws Exception {
        final ModelAndView mav = super.prepForm(model);
        return mav;
    }

    /** {@inheritDoc} */
    @Override
    protected void initBinder(final WebDataBinder binder) {
        super.initBinder(binder);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM-dd-yyyy"), true));
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add onSubmit description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param findTransactionForm
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(
        @Validated @ModelAttribute(Constants.FINDTRANSACTIONFORM) final FindTransactionForm findTransactionForm,
        final BindingResult errors) throws Exception {
        //final FindTransactionForm findTransactionForm = (FindTransactionForm) command;
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }

        final Account account = bankManager.getAccount(findTransactionForm.getAccountId());
        String accessMode = null;

        if (adminManager != null) {
            accessMode = adminManager.getParameter("accessmode");
        }
        List<Transaction> transactions = null;
        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
            final Integer transactionId = findTransactionForm.getCriteria().getTransactionId();
            if (log.isDebugEnabled()) {
                log.debug("ptocessing TransactionId: " + transactionId);
            }
            transactions = accessModeController.getTransactionsForAccount(account, findTransactionForm.getCriteria());
        } else {
            transactions = bankManager.getTransactionsForAccount(account.getId(), findTransactionForm.getCriteria());
        }

        final Map<String, Object> model = new HashMap<>();
        model.put("transactions", transactions);

        return new ModelAndView("transactionResults", "model", model);
    }

    /** {@inheritDoc} */
    @Override
    public void setAccessModeController(final AccessModeController accessModeController) {
        this.accessModeController = accessModeController;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add setAdminManager description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param adminManager
     */
    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classFindTransactionForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.FINDTRANSACTIONFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.FINDTRANS)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "findTransactionFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }

    //    @Override
    //    protected Object formBackingObject(final HttpServletRequest request) throws Exception {
    //        return new FindTransactionForm();
    //    }
    //    @Override
    //    protected void initBinder(final HttpServletRequest request, final ServletRequestDataBinder binder)
    //            throws Exception {
    //        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM-dd-yyyy"), true));
    //    }
    //
    //    @Override
    //    protected void onBindAndValidate(final HttpServletRequest request, final Object command, final BindException errors)
    //            throws Exception {
    //        final FindTransactionForm findTransactionForm = (FindTransactionForm) command;
    //
    //        final TransactionCriteria criteria = findTransactionForm.getCriteria();
    //
    //        try {
    //            errors.pushNestedPath("criteria");
    //            getValidator().validate(criteria, errors);
    //        } finally {
    //            errors.popNestedPath();
    //        }
    //    }
    //    @Override
    //    protected ModelAndView onSubmit(final Object command) throws Exception {
    //        final FindTransactionForm findTransactionForm = (FindTransactionForm) command;
    //
    //        final Account account = bankManager.getAccount(findTransactionForm.getAccountId());
    //        String accessMode = null;
    //
    //        if (adminManager != null) {
    //            accessMode = adminManager.getParameter("accessmode");
    //        }
    //
    //        if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
    //            final Integer transactionId = findTransactionForm.getCriteria().getTransactionId();
    //            if (LOG.isDebugEnabled()) {
    //                LOG.debug("ptocessing TransactionId: " + transactionId);
    //            }
    //            transactions = accessModeController.getTransactionsForAccount(account, findTransactionForm.getCriteria());
    //        } else {
    //            transactions = bankManager.getTransactionsForAccount(account.getId(), findTransactionForm.getCriteria());
    //        }
    //
    //        final Map<String, Object> model = new HashMap<String, Object>();
    //        model.put("transactions", transactions);
    //
    //        return new ModelAndView("transactionResults", "model", model);
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
