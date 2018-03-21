package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.*;

import org.junit.*;
import org.reflections.util.*;
import org.springframework.mock.web.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.util.*;

/**
 * @req PAR-8
 * @req PAR-9
 * @req PAR-10
 */
@SuppressWarnings({ "unchecked" }) public class AccountActivityControllerTest
        extends AbstractBankControllerTest<AccountActivityController> {

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