package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * @req PAR-8
 * @req PAR-9
 * @req PAR-10
 */
public class AccountActivityControllerTest extends AbstractBankControllerTest<AccountActivityController> {

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/activity.htm");
        setFormName("none");
        registerSession(request);
    }

    /**
     * Accounts details and its transactions are loaded on the frontend side by calling directly parabank api.
     * Controller returns only accountId.
     */
    @Test
    public void returnAccountId() throws Exception {
        final String id = "12345";
        request.setParameter("id", id);

        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        Integer accountId = (Integer) getModelValue(mav, "accountId");

        assertEquals(Integer.valueOf(id), accountId);
    }
}