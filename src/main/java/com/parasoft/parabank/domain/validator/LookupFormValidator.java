package com.parasoft.parabank.domain.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.web.form.LookupForm;

/**
 * Provides basic empty field validation for <code>Payee</code> object
 */
public class LookupFormValidator implements Validator {
    @Resource(name = "addressValidator")
    private Validator addressValidator;

    @Resource(name = "customerValidator")
    private Validator customerValidator;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Getter for the customerValidator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @return the value of customerValidator field
     */
    public Validator getCustomerValidator() {
        return customerValidator;
    }

    public void setAddressValidator(final Validator addressValidator) {
        this.addressValidator = addressValidator;
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>Setter for the customerValidator property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 15, 2015</DD>
     * </DL>
     *
     * @param aCustomerValidator
     *            new value for the customerValidator property
     */
    public void setCustomerValidator(final Validator aCustomerValidator) {
        customerValidator = aCustomerValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return LookupForm.class.isAssignableFrom(clazz) || Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {

        if (obj instanceof Customer) {
            getCustomerValidator().validate(obj, errors);
        } else {
            ValidationUtils.rejectIfEmpty(errors, "firstName", "error.first.name.required");
            ValidationUtils.rejectIfEmpty(errors, "lastName", "error.last.name.required");
            ValidationUtils.rejectIfEmpty(errors, "ssn", "error.ssn.required");

            final LookupForm lookup = (LookupForm) obj;
            try {
                errors.pushNestedPath("address");
                ValidationUtils.invokeValidator(addressValidator, lookup.getAddress(), errors);
            } finally {
                errors.popNestedPath();
            }
        }
    }
}
