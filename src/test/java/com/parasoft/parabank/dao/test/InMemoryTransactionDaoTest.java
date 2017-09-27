package com.parasoft.parabank.dao.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.parasoft.parabank.dao.*;
import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-26
 *
 */
public class InMemoryTransactionDaoTest extends AbstractParaBankTest {
    private static final int TRANSACTION1_ID = 1;

    private static final int TRANSACTION2_ID = 2;

    private static final int ACCOUNT_ID = 1;

    private TransactionDao transactionDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final List<Transaction> transactions = new ArrayList<Transaction>();

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION1_ID);
        transaction.setAccountId(ACCOUNT_ID);
        transactions.add(transaction);

        transaction = new Transaction();
        transaction.setId(TRANSACTION2_ID);
        transaction.setAccountId(ACCOUNT_ID);
        transactions.add(transaction);

        transactionDao = new InMemoryTransactionDao(transactions);
    }

    @Test
    public void testCreateTransaction() {
        final Transaction originalTransaction = new Transaction();
        final int id = transactionDao.createTransaction(originalTransaction);
        assertEquals(3, id);
        final Transaction newAccount = transactionDao.getTransaction(id);
        assertEquals(originalTransaction, newAccount);
    }

    @Test
    public void testGetTransaction() {
        Transaction transaction = transactionDao.getTransaction(TRANSACTION1_ID);
        assertNotNull(transaction);
        assertEquals(TRANSACTION1_ID, transaction.getId());

        transaction = transactionDao.getTransaction(TRANSACTION2_ID);
        assertNotNull(transaction);
        assertEquals(TRANSACTION2_ID, transaction.getId());

        assertNull(transactionDao.getTransaction(-1));
    }

    @Test
    public void testGetTransactionsForAccount() {
        List<Transaction> transactions = transactionDao.getTransactionsForAccount(ACCOUNT_ID);
        assertEquals(2, transactions.size());
        transactionDao.getTransactionsForAccount(ACCOUNT_ID, new TransactionCriteria());
        assertEquals(2, transactions.size());

        transactions = transactionDao.getTransactionsForAccount(-1);
        assertEquals(0, transactions.size());
        transactions = transactionDao.getTransactionsForAccount(-1, new TransactionCriteria());
        assertEquals(0, transactions.size());
    }
}
