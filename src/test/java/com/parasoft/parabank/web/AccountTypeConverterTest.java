package com.parasoft.parabank.web;

import static org.junit.Assert.*;

import org.junit.*;

import com.parasoft.parabank.domain.Account.*;
import com.parasoft.parabank.test.util.*;

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
