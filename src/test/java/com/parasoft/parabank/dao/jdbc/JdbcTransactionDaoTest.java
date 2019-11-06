package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;

import com.parasoft.parabank.dao.TransactionDao;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.Transaction.TransactionType;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

/**
 * @req PAR-20
 *
 */
// @SuppressWarnings("deprecation")
@Rollback
public class JdbcTransactionDaoTest extends AbstractParaBankDataSourceTest {

    //private static final int ACCOUNT_ID = 201;
    private static final int ACCOUNT_ID = 12456;

    private static final TransactionType TYPE = TransactionType.Debit;

    private static final Date DATE = new Date(22222);

    private static final BigDecimal AMOUNT = new BigDecimal("33333.00");

    private static final String DESCRIPTION = "44444";

    private static final Date MILLENNIAL_BEGINNING_OF_TIME = convertDate("2000-01-01");

    @Resource(name = "transactionDao")
    private TransactionDao transactionDao;

    private Transaction transaction;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        transaction = new Transaction();
        transaction.setAccountId(ACCOUNT_ID);
        transaction.setType(TYPE);
        transaction.setDate(DATE);
        transaction.setAmount(AMOUNT);
        transaction.setDescription(DESCRIPTION);
    }

    //    @Override
    //    public void onSetUpInTransaction() throws Exception {
    //        super.onSetUpInTransaction();
    //        super.executeSqlScript("classpath:com/parasoft/parabank/dao/jdbc/sql/insertCustomer.sql", true);
    //        super.executeSqlScript("classpath:com/parasoft/parabank/dao/jdbc/sql/insertAccount.sql", true);
    //    }

    public void setTransactionDao(final TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Test
    @Rollback
    public void testCreateTransaction() {
        final int id = transactionDao.createTransaction(transaction);
        assertEquals("wrong expected id?", 14476, id);

        final Transaction transaction = transactionDao.getTransaction(id);
        assertFalse(this.transaction == transaction);
        assertEquals(this.transaction, transaction);
    }

    @Test
    public void testGetTransaction() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);

        final int year = c.get(Calendar.YEAR);

        final Date today = new Date(convertDate(year + "-12-12").getTime());
        Transaction transaction = transactionDao.getTransaction(12256);
        assertEquals(12256, transaction.getId());
        assertEquals(12345, transaction.getAccountId());
        assertEquals(TransactionType.Debit, transaction.getType());
        assertEquals(today.toString(), transaction.getDate().toString());
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(new BigDecimal("100.00").floatValue(), transaction.getAmount().floatValue(), 0.0001f);
        assertEquals("Check # 1211", transaction.getDescription());

        try {
            transaction = transactionDao.getTransaction(-1);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetTransactionsForAccount() {
        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345);
        assertEquals(7, transactions.size());

        transactions = transactionDao.getTransactionsForAccount(-1);
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetTransactionsForAccountWithActivityCriterion() {
        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.ACTIVITY);

        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());

        criteria.setMonth("All");
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());

        criteria.setMonth("Invalid");
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());

        criteria.setTransactionType("Debit");
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(6, transactions.size());

        criteria.setTransactionType("Credit");
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(1, transactions.size());

        criteria.setTransactionType("All");
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());
    }

    @Test
    public void testGetTransactionsForAccountWithAmountCriterion() {
        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.AMOUNT);

        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setAmount(new BigDecimal(1000));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(3, transactions.size());

        criteria.setAmount(new BigDecimal(-1));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetTransactionsForAccountWithDateCriterion() {
        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.DATE);
        final Calendar c = Calendar.getInstance();
        //final Date today = new Date(c.getTimeInMillis());
        c.add(Calendar.DAY_OF_MONTH, -11);
        final Date tenDays = new Date(c.getTimeInMillis());

        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setOnDate(MILLENNIAL_BEGINNING_OF_TIME); //(new Date(100, 0, 1));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setOnDate(tenDays); //(new Date(110, 7, 23));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGetTransactionsForAccountWithDateRangeCriterion() {
        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.DATE_RANGE);
        final Calendar c = Calendar.getInstance();
        final Date today = new Date(c.getTimeInMillis());
        c.add(Calendar.MONTH, -1);
        final Date oneMonthAgo = new Date(c.getTimeInMillis());

        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setFromDate(MILLENNIAL_BEGINNING_OF_TIME); //(new Date(100, 0, 1));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setToDate(today); //(new Date(110, 11, 31));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());

        criteria.setFromDate(MILLENNIAL_BEGINNING_OF_TIME); //(new Date(100, 0, 1));
        criteria.setToDate(today); //(new Date(110, 11, 31));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(7, transactions.size());

        criteria.setFromDate(oneMonthAgo); //(new Date(110, 7, 1));
        criteria.setToDate(today); //(new Date(110, 7, 31));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        //assertEquals(5, transactions.size());

        //reversed dates result should be zero
        criteria.setFromDate(today); //(new Date(110, 11, 31));
        criteria.setToDate(MILLENNIAL_BEGINNING_OF_TIME); //(new Date(100, 0, 1));
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());
    }

    @Test
    public void testGetTransactionsForAccountWithIdCriterion() {
        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.ID);

        List<Transaction> transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setTransactionId(12345);
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(0, transactions.size());

        criteria.setTransactionId(14143);
        transactions = transactionDao.getTransactionsForAccount(12345, criteria);
        assertEquals(1, transactions.size());
    }
}