package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.AdminParameters;
import com.parasoft.parabank.util.Constants;

/**
 * @req PAR-20
 * @req PAR-21
 * @req PAR-43
 * @req PAR-26
 * @req PAR-42
 *
 */
// @SuppressWarnings({ "unchecked" })
public class OpenAccountControllerTest extends AbstractBankControllerTest<OpenAccountController> {
    @Resource(name = "adminManager")
    private AdminManager adminManager;

    private void assertReferenceData(final ModelAndView mav) {
// direct rest call is used to obtain accounts (/parabank/services/bank/customers/{customerId}/accounts)
//        @SuppressWarnings("unchecked")
//        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
//        assertEquals(11, accounts.size());
        @SuppressWarnings("unchecked")
        final List<AccountType> types = (List<AccountType>) mav.getModel().get("types");
        final int customerId = (int) mav.getModel().get("customerId");
        assertEquals(2, types.size());
        assertEquals(adminManager.getParameter(AdminParameters.MINIMUM_BALANCE), mav.getModel().get("minimumBalance"));
        assertEquals(12212, customerId);
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //        controller.setCommandClass(OpenAccountForm.class);
        //        controller.setAdminManager(adminManager);
        setPath("/openaccount.htm");
        setFormName(Constants.OPENACCOUNTFORM);
        registerSession(request);

    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
        //final ModelAndView mav = controller.handleRequest(request, response);
        assertReferenceData(mav);
    }

// direct rest call is used to create new account (parabank/services/bank/createAccount)
//    @Test
//    @Transactional
//    @Rollback
//    public void testOnSubmit() throws Exception {
//        ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());
//        request = registerSession(new MockHttpServletRequest());
//        final OpenAccountForm form = (OpenAccountForm) mav.getModel().get(getFormName());
//        form.setFromAccountId(12345);
//        form.setType(AccountType.CHECKING);
//
//        final Account oldAccount = bankManager.getAccount(12345);
//        List<Transaction> transactions = bankManager.getTransactionsForAccount(oldAccount);
//        assertEquals(7, transactions.size());
//
//        mav = processPostRequest(form, request, new MockHttpServletResponse());
//        //final BindException errors = new BindException(form, Constants.OPENACCOUNTFORM);
//        //final ModelAndView mav = controller.onSubmit(request, response, form, errors);
//
//        assertEquals("openaccountConfirm", mav.getViewName());
//        Account newAccount = (Account) mav.getModel().get("account");
//        assertEquals(13566, newAccount.getId());
//
//        newAccount = bankManager.getAccount(newAccount.getId());
//        assertEquals(new BigDecimal(adminManager.getParameter(AdminParameters.MINIMUM_BALANCE)),
//            newAccount.getBalance());
//
//        transactions = bankManager.getTransactionsForAccount(oldAccount);
//        assertEquals(8, transactions.size());
//
//        transactions = bankManager.getTransactionsForAccount(newAccount);
//        assertEquals(1, transactions.size());
//    }
}
