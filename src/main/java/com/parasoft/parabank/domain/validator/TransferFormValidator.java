package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.web.form.TransferForm;

/**
 * Provides basic empty field validation for RequestLoanForm object
 */
public class TransferFormValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return TransferForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "amount", "error.amount.empty");
    }
}
