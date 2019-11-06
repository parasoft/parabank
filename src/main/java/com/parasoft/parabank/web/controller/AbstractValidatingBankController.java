package com.parasoft.parabank.web.controller;

import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Abstract controller that validates user input
 */
abstract class AbstractValidatingBankController extends AbstractBankController {

    protected Validator validator;
    // @Override
    // protected abstract void onBindAndValidate(HttpServletRequest request,
    // Object command, BindException errors)
    // throws Exception;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the validator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @return the value of validator field
     */
    public Validator getValidator() {
        return validator;
    }

    @InitBinder
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(getValidator());
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the validator property, must be implemented by descendants
     * adding a <code>Resource</code> annotation with the name of the actual
     * <code>Validator</code> implementation</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aValidator
     *            new value for the validator property
     */
    public abstract void setValidator(Validator aValidator);
}
