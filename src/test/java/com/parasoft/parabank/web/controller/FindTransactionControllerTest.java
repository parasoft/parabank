package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.util.Constants;
import com.parasoft.parabank.web.form.FindTransactionForm;

/**
 * @req PAR-11
 * @req PAR-21
 * @req PAR-31
 * @req PAR-30
 *
 */
// @SuppressWarnings({ "unchecked" })
public class FindTransactionControllerTest extends AbstractValidatingBankControllerTest<FindTransactionController> {
    //@SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(FindTransactionControllerTest.class);

    private void assertReferenceData(final ModelAndView mav) {
        @SuppressWarnings("unchecked")
        final List<Account> accounts = (List<Account>) mav.getModel().get("accounts");
        assertEquals(11, accounts.size());
    }

    public void assertTransactions(final FindTransactionForm form, final int expectedSize) throws Exception {
        final ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //final ModelAndView mav = controller.onSubmit(form);
        assertEquals("transactionResults", mav.getViewName());
        @SuppressWarnings("unchecked")
        final List<Transaction> transactions = (List<Transaction>) getModelValue(mav, "transactions");
        assertEquals(expectedSize, transactions.size());
    }

    private FindTransactionForm getFindTransactionForm() {
        final FindTransactionForm form = new FindTransactionForm();
        form.setAccountId(12345);
        final TransactionCriteria criteria = new TransactionCriteria();
        form.setCriteria(criteria);
        return form;
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setValidator(new TransactionCriteriaValidator());
        setPath("/findtrans.htm");
        setFormName(Constants.FINDTRANSACTIONFORM);
        registerSession(request);
    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final ModelAndView mav =
            processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //final ModelAndView mav = controller.handleRequest(request, response);
        assertReferenceData(mav);
    }

    @Test
    public void testOnSubmit() throws Exception {
        final Calendar c = Calendar.getInstance();
        final Date today = new Date(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH, -11); //11 days ago (2 transactions)
        final Date elevenDaysAgo = new Date(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH, 11); //forward to today
        c.add(Calendar.MONTH, -1); //back one month
        final Date oneMonthAgo = new Date(c.getTimeInMillis());
        log.debug("----------------------------------------------");
        log.debug("elevenDaysAgo : {}", elevenDaysAgo.toString());
        log.debug("----------------------------------------------");
        FindTransactionForm form = getFindTransactionForm();
        form.getCriteria().setSearchType(SearchType.ACTIVITY);
        assertTransactions(form, 7);

        form = getFindTransactionForm();
        form.getCriteria().setSearchType(SearchType.ID);
        form.getCriteria().setTransactionId(14143);
        assertTransactions(form, 1);

        form = getFindTransactionForm();
        form.getCriteria().setSearchType(SearchType.DATE);
        form.getCriteria().setOnDate(elevenDaysAgo);//new Date(110, 7, 23));
        assertTransactions(form, 2);

        final int month = Calendar.getInstance().get(Calendar.MONTH);
        if (month != 0 && month != 11) {
            form = getFindTransactionForm();
            form.getCriteria().setSearchType(SearchType.DATE_RANGE);
            form.getCriteria().setFromDate(oneMonthAgo);//new Date(110, 7, 1));
            form.getCriteria().setToDate(today);//new Date(110, 7, 31));
            assertTransactions(form, 5);
        }

        form = getFindTransactionForm();
        form.getCriteria().setSearchType(SearchType.AMOUNT);
        form.getCriteria().setAmount(new BigDecimal(1000));
        assertTransactions(form, 3);
    }

    @Test
    public void testValidate() throws Exception {
        final FindTransactionForm form = getFindTransactionForm();
        form.getCriteria().setSearchType(SearchType.ID);
        form.getCriteria().setTransactionId(null);
        ModelAndView mav =
            processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "criteria.transactionId", "error.empty.transaction.id");

        form.getCriteria().setSearchType(SearchType.DATE);
        form.getCriteria().setOnDate(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "criteria.onDate", "error.empty.on.date");

        form.getCriteria().setSearchType(SearchType.DATE_RANGE);
        form.getCriteria().setFromDate(null);
        form.getCriteria().setToDate(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        getErrorMap().clear();
        getErrorMap().put("criteria.fromDate", "error.empty.from.date");
        getErrorMap().put("criteria.toDate", "error.empty.to.date");
        assertError(mav, getErrorMap());

        form.getCriteria().setSearchType(SearchType.AMOUNT);
        form.getCriteria().setOnDate(null);
        mav = processPostRequest(form, registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertError(mav, "criteria.amount", "error.empty.amount");
    }
}
//"criteria.transactionId", "error.empty.transaction.id"
//"criteria.onDate", "error.empty.on.date"
//"criteria.fromDate", "error.empty.from.date"
//"criteria.toDate", "error.empty.to.date"
//"criteria.amount", "error.empty.amount"
//"criteria.month", "error.empty.month"
//"criteria.transactionType", "error.empty.transaction.type"