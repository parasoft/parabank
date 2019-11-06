package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.web.form.RequestLoanForm;

/**
 * Provides basic empty field validation for RequestLoanForm object
 */
public class RequestLoanFormValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return RequestLoanForm.class.isAssignableFrom(clazz) || LoanResponse.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        // final RequestLoanForm rlform = (RequestLoanForm) obj;
        ValidationUtils.rejectIfEmpty(errors, "amount", "error.loan.amount.empty");
        ValidationUtils.rejectIfEmpty(errors, "downPayment", "error.down.payment.empty");
    }
}
