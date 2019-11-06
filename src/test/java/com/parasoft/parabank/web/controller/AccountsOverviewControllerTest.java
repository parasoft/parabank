package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * @req PAR-11
 * @req PAR-9
 * @req PAR-10
 */
public class AccountsOverviewControllerTest extends AbstractBankControllerTest<AccountsOverviewController> {

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
