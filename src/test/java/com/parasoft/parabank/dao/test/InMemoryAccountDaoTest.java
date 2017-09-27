package com.parasoft.parabank.dao.test;

import static org.junit.Assert.*;

import java.math.*;
import java.util.*;

import org.junit.*;

import com.parasoft.parabank.dao.*;
import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-21
 *
 */
public class InMemoryAccountDaoTest extends AbstractParaBankTest {
    private static final int ACCOUNT1_ID = 1;

    private static final int ACCOUNT2_ID = 2;

    private AccountDao accountDao;

    @Override
    public void setUp() throws Exception {
        final List<Account> accounts = new ArrayList<Account>();

        Account account = new Account();
        account.setId(ACCOUNT1_ID);
        accounts.add(account);

        account = new Account();
        account.setId(ACCOUNT2_ID);
        accounts.add(account);

        accountDao = new InMemoryAccountDao(accounts);
    }

    @Test
    public void testCreateAccount() {
        final Account originalAccount = new Account();
        final int id = accountDao.createAccount(originalAccount);
        assertEquals(3, id);
        final Account newAccount = accountDao.getAccount(id);
        assertEquals(originalAccount, newAccount);
    }

    @Test
    public void testGetAccount() {
        Account account = accountDao.getAccount(ACCOUNT1_ID);
        assertNotNull(account);
        assertEquals(ACCOUNT1_ID, account.getId());

        account = accountDao.getAccount(ACCOUNT2_ID);
        assertNotNull(account);
        assertEquals(ACCOUNT2_ID, account.getId());

        assertNull(accountDao.getAccount(-1));
    }

    @Test
    public void testUpdateAccount() {
        Account account = accountDao.getAccount(ACCOUNT1_ID);
        account.setBalance(new BigDecimal("100.00"));
        accountDao.updateAccount(account);
        account = accountDao.getAccount(ACCOUNT1_ID);
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(new BigDecimal("100.00").floatValue(), account.getBalance().floatValue(), 0.0001f);
    }
}
