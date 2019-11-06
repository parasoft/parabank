package com.parasoft.parabank.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-45
 *
 */
public class AccountTypeConverterTest extends AbstractParaBankTest {
    @Test
    public void testConvert() {
        final AccountTypeConverter converter = new AccountTypeConverter();
        assertNull(converter.convert(null));
        assertEquals(AccountType.CHECKING, converter.convert("CHECKING"));
        assertEquals(AccountType.SAVINGS, converter.convert("SAVINGS"));

        try {
            converter.convert("");
            fail("did not catch expected IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
        }
    }
}
