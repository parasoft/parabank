package com.parasoft.parabank.domain.validator;

import org.springframework.validation.Validator;

import com.parasoft.parabank.domain.Address;

/**
 * @req PAR-35
 *
 */
public class AddressValidatorTest extends AbstractValidatorTest {
    public AddressValidatorTest() {
        super(Address.class, new String[] { "street", "city", "state", "zipCode" });
    }

    @Override
    protected Validator getValidator() {
        return new AddressValidator();
    }
}
