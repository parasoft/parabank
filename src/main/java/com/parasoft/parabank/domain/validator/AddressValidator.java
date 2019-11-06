package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Address;

/**
 * Provides basic empty field validation for Address object
 */
public class AddressValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return Address.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "street", "error.address.required");
        ValidationUtils.rejectIfEmpty(errors, "city", "error.city.required");
        ValidationUtils.rejectIfEmpty(errors, "state", "error.state.required");
        ValidationUtils.rejectIfEmpty(errors, "zipCode", "error.zip.code.required");
    }
}
