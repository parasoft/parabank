package com.parasoft.parabank.domain.validator;

import javax.annotation.Resource;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Customer;

/**
 * Provides basic empty field validation for Customer object
 */
public class CustomerValidator implements Validator {
    @Resource(name = "addressValidator")
    private Validator addressValidator;

    public void setAddressValidator(final Validator addressValidator) {
        this.addressValidator = addressValidator;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "firstName", "error.first.name.required");
        ValidationUtils.rejectIfEmpty(errors, "lastName", "error.last.name.required");
        ValidationUtils.rejectIfEmpty(errors, "ssn", "error.ssn.required");
        ValidationUtils.rejectIfEmpty(errors, "username", "error.username.required");
        ValidationUtils.rejectIfEmpty(errors, "password", "error.password.required");

        final Customer customer = (Customer) obj;
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(addressValidator, customer.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
