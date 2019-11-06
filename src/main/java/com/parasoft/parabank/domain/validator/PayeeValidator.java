package com.parasoft.parabank.domain.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Payee;

/**
 * Provides basic empty field validation for <code>Payee</code> object
 */
public class PayeeValidator implements Validator {
    @Resource(name = "addressValidator")
    private Validator addressValidator;

    public void setAddressValidator(final Validator addressValidator) {
        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return Payee.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "error.payee.name.required");
        ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "error.phone.number.required");
        ValidationUtils.rejectIfEmpty(errors, "accountNumber", "error.account.number.required");

        final Payee payee = (Payee) obj;
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(addressValidator, payee.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
