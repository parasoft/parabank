package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.TransferForm;

/**
 * @req PAR-27
 * @req PAR-16
 * @req PAR-25
 *
 */
@SuppressWarnings({ "unchecked" })
public class TransferControllerTest extends AbstractBankControllerTest<TransferController> {
    private void assertReferenceData(final ModelAndView mav) {
        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
        assertEquals(11, accounts.size());
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        controller.setCommandClass(TransferForm.class);
        setPath("/transfer.htm");
        setFormName(Constants.TRANSFERFORM);
        registerSession(request);
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        //final ModelAndView mav = controller.handleRequest(request, response);
        assertReferenceData(mav);
    }

//    @Test
//    @Transactional
//    @Rollback
//    public void testOnSubmit() throws Exception {
//        //final TransferForm form = new TransferForm();
//        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
//        final TransferForm form = (TransferForm) mav.getModel().get(getFormName());
//        form.setAmount(new BigDecimal(100));
//        form.setFromAccountId(12345);
//        form.setToAccountId(54321);
//
//        //final ModelAndView mav = controller.onSubmit(form);
//        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
//        assertEquals("transferConfirm", mav.getViewName());
//        assertEquals(new BigDecimal(100), getModelValue(mav, "amount"));
//        assertEquals(12345, getModelValue(mav, "fromAccountId"));
//        assertEquals(54321, getModelValue(mav, "toAccountId"));
//    }
//
//    @Test
//    public void testValidate() throws Exception {
//        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
//        final TransferForm form = (TransferForm) mav.getModel().get(getFormName());
//        form.setAmount(null);
//        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
//        assertError(mav, "amount", "error.amount.empty");
//        //final TransferForm form = new TransferForm();
//        //final BindException errors = new BindException(form, Constants.TRANSFERFORM);
//        //controller.onBindAndValidate(request, form, errors);
//        //assertEquals(1, errors.getErrorCount());
//        //assertNotNull(errors.getFieldError("amount"));
//    }
}
