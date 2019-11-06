package com.parasoft.parabank.web.controller;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.impl.LoanProviderMapAware;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>This class is used as a base class for AdminController JMSListenerController and DatabaseController Since they
 * all need nearly identical functionality</DD>
 * <DT>Date:</DT>
 * <DD>Oct 24, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public abstract class AbstractBaseAdminController extends AbstractValidatingBankController {

    protected static final Logger log = LoggerFactory.getLogger(DatabaseController.class);

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Resource(name = "loanProvider")
    private LoanProviderMapAware loanProvider;

    @Resource(name = "loanProcessor")
    private LoanProviderMapAware loanProcessor;

    public AbstractBaseAdminController() {
        super();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the adminManager property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @return the value of adminManager field
     */
    public AdminManager getAdminManager() {
        return adminManager;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>populate the AdminForm from defaults needed for DatabaseController and JMSListenerController</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @param model
     * @return
     * @throws Exception
     */
    public AdminForm getForm() throws Exception {
        final AdminForm form = getAdminManager().populateAdminForm(new AdminForm());
        return form;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the loanProcessor property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @return the value of loanProcessor field
     */
    public LoanProviderMapAware getLoanProcessor() {
        return loanProcessor;
    }

    @ModelAttribute("loanProcessors")
    public Set<String> getLoanProcessors() {
        return loanProcessor.getProviderNames();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the loanProvider property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @return the value of loanProvider field
     */
    public LoanProviderMapAware getLoanProvider() {
        return loanProvider;
    }

    @ModelAttribute("loanProviders")
    public Set<String> getProviderNames() {
        return loanProvider.getProviderNames();
    }

    @ModelAttribute("isJmsRunning")
    public boolean isJmsListenerRunning() {
        return getAdminManager().isJmsListenerRunning();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>TODO add saveAdminSettings description</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @param model
     */
    public void saveAdminSettings(final AdminForm form) {
        log.info("Saving current admin settings.");
        for (final Entry<String, String> entry : form.getParameters().entrySet()) {
            getAdminManager().setParameter(entry.getKey(), entry.getValue());
        }
        log.info("Done Saving current admin settings.");
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not used

    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = "classAdminForm")
    public void setCommandClass(final Class<?> aCommandClass) {
        super.setCommandClass(aCommandClass);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.ADMINFORM)
    public void setCommandName(final String aCommandName) {
        super.setCommandName(aCommandName);
    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.ADMIN)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    public void setLoanProcessor(final LoanProviderMapAware loanProcessor) {
        this.loanProcessor = loanProcessor;
    }

    public void setLoanProvider(final LoanProviderMapAware loanProvider) {
        this.loanProvider = loanProvider;
    }

    @Override
    @Resource(name = "adminFormValidator")
    public void setValidator(final Validator aValidator) {
        validator = aValidator;
    }
    // @Override
    // protected void onBindAndValidate(final HttpServletRequest request, final
    // Object command, final BindException errors)
    // throws Exception {
    // ValidationUtils.rejectIfEmpty(errors, "initialBalance",
    // "error.initial.balance.required");
    // ValidationUtils.rejectIfEmpty(errors, "minimumBalance",
    // "error.minimum.balance.required");
    // ValidationUtils.rejectIfEmpty(errors, "loanProcessorThreshold",
    // "error.loan.processor.threshold.required");
    // }
    // @Override
    // protected ModelAndView onSubmit(final HttpServletRequest request, final
    // HttpServletResponse response,
    // final Object command, final BindException errors) throws Exception {
    // final AdminForm form = (AdminForm) command;
    //
    // final ModelAndView modelAndView = super.showForm(request, response,
    // errors);
    //
    // for (final Entry<String, String> entry : form.getParameters().entrySet())
    // {
    // adminManager.setParameter(entry.getKey(), entry.getValue());
    // log.info("Using regular JDBC connection");
    // }
    //
    // modelAndView.addObject("message", "settings.saved");
    //
    // return modelAndView;
    // }
    // @Override
    // protected Map<String, Object> referenceData(final HttpServletRequest
    // request) throws Exception {
    // final Map<String, Object> model = new HashMap<String, Object>();
    //
    // model.put("isJmsRunning", adminManager.isJmsListenerRunning());
    // model.put("loanProviders", loanProvider.getProviderNames());
    // model.put("loanProcessors", loanProcessor.getProviderNames());
    //
    // return model;
    // }

}
