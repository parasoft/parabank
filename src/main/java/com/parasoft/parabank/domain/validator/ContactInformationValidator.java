/*
 * (C) Copyright Parasoft Corporation 2019.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.ContactInformation;

public class ContactInformationValidator implements Validator {
    @Override
    public boolean supports(final Class<?> clazz) {
        return ContactInformation.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "error.phone.number.required");
    }
}
