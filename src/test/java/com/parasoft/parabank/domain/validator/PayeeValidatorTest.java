package com.parasoft.parabank.domain.validator;

import org.springframework.validation.*;

import com.parasoft.parabank.domain.*;

public class PayeeValidatorTest extends AbstractValidatorTest {
    public PayeeValidatorTest() {
        super(Payee.class, new String[] { "name", "phoneNumber", "accountNumber" });
    }

    @Override
    protected Validator getValidator() {
        PayeeValidator validator = new PayeeValidator();
        validator.setAddressValidator(new AddressValidator());
        return validator;
    }
}
