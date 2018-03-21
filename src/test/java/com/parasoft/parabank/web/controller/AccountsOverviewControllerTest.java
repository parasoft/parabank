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
 */
@SuppressWarnings("unchecked") public class AccountsOverviewControllerTest
        extends AbstractBankControllerTest<AccountsOverviewController> {

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/overview.htm");
        setFormName("none");
        registerSession(request);

    }
    /**
     * Accounts details are loaded on the frontend side by calling directly parabank rest api.
     * Controller returns only customerId.
     */
    @Test
    public void overviewAccountsControllerShouldReturnOnlyCustomerId() throws Exception {
        Integer customerId = 12212;
        request = registerSession(new MockHttpServletRequest(), customerId);

        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        Integer actualCustomerId = (Integer) getModelValue(mav, "customerId");

        assertEquals(customerId, actualCustomerId);
    }
}
