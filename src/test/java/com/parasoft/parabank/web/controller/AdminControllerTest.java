package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.impl.LoanProviderMapAware;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.AdminForm;

/**
 * @req PAR-8
 * @req PAR-6
 * @req PAR-7
 *
 */
public class AdminControllerTest extends AbstractValidatingBankControllerTest<AdminController> {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(RegisterCustomerControllerTest.class);

    @Resource(name = "adminManager")
    private AdminManager adminManager;

    @Resource(name = "loanProvider")
    private LoanProviderMapAware loanProvider;

    private AdminForm getAdminForm() {
        final AdminForm form = new AdminForm();
        form.setInitialBalance(new BigDecimal("1111.11"));
        form.setMinimumBalance(new BigDecimal("2222.22"));
        form.setLoanProcessorThreshold(3333);
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/admin.htm");
        setFormName(Constants.ADMINFORM);

        //        controller.setAdminManager(adminManager);
        //        controller.setLoanProvider(loanProvider);
        //        controller.setLoanProcessor(loanProvider);
        //        controller.setCommandClass(AdminForm.class);
    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    public void setLoanProvider(final LoanProviderMapAware loanProvider) {
        this.loanProvider = loanProvider;
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final AdminForm form = (AdminForm) mav.getModel().get(getFormName());
        assertNotNull("AdminForm was not returned by a get request ", form);
        //TODO Why is the JMS tested here? it may or may not be running at this time
        //        final boolean isJmsRunning = (Boolean) mav.getModel().get("isJmsRunning");
        //        assertTrue(isJmsRunning);
    }

    @Test
    @Transactional
    @Rollback
    public void testOnSubmit() throws Exception {
        //final AdminForm form = (AdminForm) controller.formBackingObject(new MockHttpServletRequest());
        //       mav = processPutRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        final AdminForm form = (AdminForm) mav.getModel().get(getFormName());
        assertNotNull("AdminForm was not returned by a get request ", form);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        //final BindException errors = new BindException(form, Constants.ADMINFORM);
        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
        assertEquals("settings.saved", mav.getModel().get("message").toString());
    }

    @Test
    @Transactional
    @Rollback
    public void testValidator() throws Exception {
        AdminForm form = getAdminForm();
        form.setInitialBalance(null);
        ModelAndView mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertError(mav, "initialBalance", "error.initial.balance.required");

        form = getAdminForm();
        form.setMinimumBalance(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertError(mav, "minimumBalance", "error.minimum.balance.required");

        form = getAdminForm();
        form.setLoanProcessorThreshold(null);
        mav = processPostRequest(form, new MockHttpServletRequest(), new MockHttpServletResponse());
        assertError(mav, "loanProcessorThreshold", "error.loan.processor.threshold.required");
    }
}
