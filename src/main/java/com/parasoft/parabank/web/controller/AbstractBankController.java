package com.parasoft.parabank.web.controller;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

// import org.springframework.web.servlet.mvc.SimpleFormController;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.util.IAccessModeControllerAware;

/**
 * Abstract controller that depends on banking functions
 */
// @SuppressWarnings("deprecation")
// abstract class AbstractBankController extends SimpleFormController {
abstract class AbstractBankController implements IAccessModeControllerAware, ApplicationContextAware {

    ApplicationContext applicationContext;

    private String formView;

    private String commandName;

    private Class<?> commandClass;

    @Resource(name = "bankManager")
    protected BankManager bankManager;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the applicationContext property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 25, 2015</DD>
     * </DL>
     *
     * @return the value of applicationContext field
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the bankManager property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 24, 2015</DD>
     * </DL>
     *
     * @return the value of bankManager field
     */
    public BankManager getBankManager() {
        return bankManager;
    }

    protected <T> T getCommand(final Model model) throws Exception {
        @SuppressWarnings("unchecked")
        final T command = (T) model.asMap().get(getCommandName());
        return command;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the commandClass property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of commandClass field
     */
    public Class<?> getCommandClass() {
        return commandClass;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the commandName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of commandName field
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the formView property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of formView field
     */
    public String getFormView() {
        return formView;
    }

    protected <T> T prepCommand(final Model model) throws Exception {
        @SuppressWarnings("unchecked")
        final T command = (T) getCommandClass().newInstance();
        return prepCommand(model, command);
    }

    protected <T> T prepCommand(final Model model, final T command) throws Exception {
        model.addAttribute(getCommandName(), command);
        return command;
    }

    protected ModelAndView prepForm(final Model model) throws Exception {
        final Object command = getCommandClass().newInstance();
        return prepForm(model, command);
    }

    protected ModelAndView prepForm(final Model model, final Object command) {
        final ModelAndView mav = new ModelAndView(getFormView());
        if (model == null) {
            mav.addObject(getCommandName(), command);
        } else {
            model.addAttribute(getCommandName(), command);
            mav.addAllObjects(model.asMap());
        }
        return mav;
    }

    /** {@inheritDoc} */
    @Override
    public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException {
        applicationContext = aApplicationContext;
    }

    public final void setBankManager(final BankManager bankManager) {
        this.bankManager = bankManager;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the commandClass property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aCommandClass
     *            new value for the commandClass property
     */
    public void setCommandClass(final Class<?> aCommandClass) {
        commandClass = aCommandClass;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the commandName property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aCommandName
     *            new value for the commandName property
     */
    public void setCommandName(final String aCommandName) {
        commandName = aCommandName;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the formView property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aFormView
     *            new value for the formView property
     */
    public void setFormView(final String aFormView) {
        formView = aFormView;
    }

}
