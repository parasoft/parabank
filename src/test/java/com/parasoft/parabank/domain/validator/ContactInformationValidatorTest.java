/*
 * (C) Copyright Parasoft Corporation 2019.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.ContactInformation;

public class ContactInformationValidatorTest extends AbstractValidatorTest {
    public ContactInformationValidatorTest() {
        super(ContactInformation.class, new String[] { "(808) 555-5555", "contact@parasoft.com" });
    }

    @Override
    protected Validator getValidator() {
        return new ContactInformationValidator();
    }
}
