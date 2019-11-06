package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.web.form.ContactForm;

public class ContactFormValidator implements Validator {
    @SuppressWarnings("unused")
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ContactFormValidator.class);

    @Override
    public boolean supports(final Class<?> aParamClass) {
        return ContactForm.class.isAssignableFrom(aParamClass);
    }

    @Override
    public void validate(final Object contactFormObj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "name", "error.name.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "error.email.empty");
        ValidationUtils.rejectIfEmpty(errors, "phone", "error.phone.empty");
        ValidationUtils.rejectIfEmpty(errors, "message", "error.message.empty");
    }

}
