package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Customer;

public class CustomerValidatorTest extends AbstractValidatorTest {
    public CustomerValidatorTest() {
        super(Customer.class, new String[] { "firstName", "lastName", "ssn", "username", "password" });
    }

    @Override
    protected Validator getValidator() {
        CustomerValidator validator = new CustomerValidator();
        validator.setAddressValidator(new AddressValidator());
        return validator;
    }
}
