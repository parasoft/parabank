package com.parasoft.parabank.web.controller;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.Transaction.TransactionType;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.service.ParaBankServiceException;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.util.Util;
import com.parasoft.parabank.web.ViewUtil;

/**
 * Controller for displaying user account activity
 */
// @SuppressWarnings("deprecation")
@Controller("secure_activity")
@RequestMapping("/activity.htm")
@SessionAttributes(Constants.TRANSACTIONCRITERIA)
public class AccountActivityController extends AbstractBankController {

    private static final Logger log = LoggerFactory.getLogger(AccountActivityController.class);

    @Resource(name = "accessModeController")
    private AccessModeController accessModeController;

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @ModelAttribute("months")
    public List<String> getMonths(final Locale locale) {
        final List<String> months = new ArrayList<>(Arrays.asList(new DateFormatSymbols(locale).getMonths()));
        months.add(0, "All");
        months.remove(13);
        return months;
    }

    @ModelAttribute("types")
    public List<String> getTypes() {
        final List<String> types = new ArrayList<>();
        for (final TransactionType type : TransactionType.values()) {
            types.add(type.toString());
        }
        types.add(0, "All");
        return types;
    }

    private ModelAndView loadTransactions(final String id, final TransactionCriteria criteria,
        final ModelAndView modelAndView) throws ParaBankServiceException, IOException, JAXBException {

        Account account;
        List<Transaction> transactions;
        final Map<String, Object> model = new HashMap<>();

        // final String id = request.getParameter("id");
        if (Util.isEmpty(id)) {
            log.error("Missing required account id");
            return ViewUtil.createErrorView("error.missing.account.id");
        }

        try {

            String accessMode = null;

            if (adminManager != null) {
                accessMode = adminManager.getParameter("accessmode");
            }

            if (accessMode != null && !accessMode.equalsIgnoreCase("jdbc")) {
                account = accessModeController.doGetAccount(Integer.parseInt(id));
                transactions = accessModeController.getTransactionsForAccount(account, criteria);
            }

            else {
                // default JDBC
                account = bankManager.getAccount(Integer.parseInt(id));

                transactions = criteria == null ? bankManager.getTransactionsForAccount(account)
                    : bankManager.getTransactionsForAccount(account.getId(), criteria);
            }
            log.info("got {} transactions for account id: {}", transactions == null ? 0 : transactions.size(), account);

            model.put("accountId", account.getId());
            modelAndView.addObject("model", model);
        } catch (final NumberFormatException e) {
            log.error("Invalid account id = " + id, e);
            return ViewUtil.createErrorView("error.invalid.account.id", new Object[] { id });
        } catch (final DataAccessException e) {
            log.error("Invalid account id = " + id, e);
            return ViewUtil.createErrorView("error.invalid.account.id", new Object[] { id });
        } catch (final ParseException e) {
            log.error("Error retrieving transactions for account " + id);
            throw new ParaBankServiceException("Error retreiving transactions");
        }

        return modelAndView;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add onSubmit description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param id
     * @param errors
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@ModelAttribute(Constants.TRANSACTIONCRITERIA) final TransactionCriteria criteria,
        final BindingResult errors, @RequestParam("id") final String id, final Model model) throws Exception {
        if (errors.hasErrors()) {
            return new ModelAndView(getFormView(), errors.getModel());
        }
        //TODO: consider adding validation to criteria
        final ModelAndView modelAndView = new ModelAndView(getFormView(), model.asMap());
        //modelAndView.addAllObjects(model.asMap());

        //final TransactionCriteria criteria = super.getCommand(model);
        // (TransactionCriteria) model.asMap().get(getCommandName());
        criteria.setSearchType(SearchType.ACTIVITY);

        return loadTransactions(id, criteria, modelAndView);
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        accessModeController = aAccessModeController;
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classTransactionCriteria")
    //@Value("#{com.parasoft.parabank.util.Constants.CLASS_TRANSACTIONCRITERIA}")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.TRANSACTIONCRITERIA)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.ACTIVITY)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add showForm description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param id
     * @param errors
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView showForm(@RequestParam("id") final String id, final Model model) throws Exception {
        final ModelAndView modelAndView = prepForm(model);

        return loadTransactions(id, null, modelAndView);
    }

    // @Override
    // protected ModelAndView onSubmit(final HttpServletRequest request, final
    // HttpServletResponse response,
    // final Object command, final BindException errors, Model model) throws
    // Exception {
    // final ModelAndView modelAndView = super.showForm(request, response,
    // errors);
    //
    // final TransactionCriteria criteria = (TransactionCriteria) command;
    // criteria.setSearchType(SearchType.ACTIVITY);
    //
    // return loadTransactions(request, criteria, modelAndView);
    // }
    // @Override
    // protected Map<String, Object> referenceData(HttpServletRequest request)
    // throws Exception {
    // Map<String, Object> model = new HashMap<String, Object>();
    //
    // List<String> months = new ArrayList<String>(Arrays.asList(new
    // DateFormatSymbols(request.getLocale()).getMonths()));
    // months.add(0, "All");
    // months.remove(13);
    //
    // List<String> types = new ArrayList<String>();
    // for (TransactionType type : TransactionType.values()) {
    // types.add(type.toString());
    // }
    // types.add(0, "All");
    //
    // model.put("months", months);
    // model.put("types", types);
    //
    // return model;
    // }
    // @Override
    // protected ModelAndView showForm(final HttpServletRequest request, final
    // HttpServletResponse response,
    // final BindException errors) throws Exception {
    // final ModelAndView modelAndView = super.showForm(request, response,
    // errors);
    //
    // return loadTransactions(request, null, modelAndView);
    // }

}
