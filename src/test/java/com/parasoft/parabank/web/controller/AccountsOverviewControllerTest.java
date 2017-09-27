package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.springframework.mock.web.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;

/**
 * @req PAR-11
 * @req PAR-9
 * @req PAR-10
 *
 */
@SuppressWarnings("unchecked")
public class AccountsOverviewControllerTest extends AbstractBankControllerTest<AccountsOverviewController> {
    private void assertUserAccounts(final int id, final int expectedSize) throws Exception {
        request = registerSession(new MockHttpServletRequest(), id);
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final List<Account> accounts = (List<Account>) getModelValue(mav, "accounts");
        assertEquals(expectedSize, accounts.size());
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/overview.htm");
        setFormName("none");
        registerSession(request);

    }

    @Test
    public void testHandleRequest() throws Exception {
        assertUserAccounts(12212, 11);
        assertUserAccounts(12323, 1);
        assertUserAccounts(3, 0);
    }
}
