package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;

import com.parasoft.parabank.dao.AccountDao;
import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

/**
 * @req PAR-11
 *
 */
@Rollback
public class JdbcAccountDaoTest extends AbstractParaBankDataSourceTest {
    //private static final int CUSTOMER_ID = 101;
    private static final int CUSTOMER_ID = 12323;

    private static final AccountType TYPE = AccountType.SAVINGS;

    private static final BigDecimal BALANCE = new BigDecimal("22222.00");

    @Resource(name = "accountDao")
    private AccountDao accountDao;

    private Account account;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        account = new Account();
        account.setCustomerId(CUSTOMER_ID);
        account.setType(TYPE);
        account.setBalance(BALANCE);
    }

    //    @Override
    //    public void onSetUpInTransaction() throws Exception {
    //        super.onSetUpInTransaction();
    //        super.executeSqlScript("classpath:com/parasoft/parabank/dao/jdbc/sql/insertCustomer.sql", true);
    //    }

    public void setAccountDao(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Test
    public void testCreateAccount() {
        final int id = accountDao.createAccount(account);
        assertEquals("wrong expected id?", 13566, id);

        final Account account = accountDao.getAccount(id);
        assertFalse(this.account == account);
        assertEquals(this.account, account);
    }

    @Test
    public void testGetAccount() {
        Account account = accountDao.getAccount(13455);
        assertEquals(13455, account.getId());
        assertEquals(12323, account.getCustomerId());
        assertEquals(AccountType.CHECKING, account.getType());
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(new BigDecimal("2014.76").floatValue(), account.getBalance().floatValue(), 0.0001f);

        try {
            account = accountDao.getAccount(-1);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetAccountsForCustomerId() {
        List<Account> accounts = accountDao.getAccountsForCustomerId(12212);
        assertEquals("wrong number of accounts?", 11, accounts.size());

        accounts = accountDao.getAccountsForCustomerId(-1);
        assertEquals("expected no accounts for invalid id", 0, accounts.size());
    }

    @Test
    public void testUpdateAccount() {
        final int id = accountDao.createAccount(account);

        final Account account = accountDao.getAccount(id);
        assertFalse(this.account == account);
        assertEquals(this.account, account);

        //account.setCustomerId(account.getCustomerId() + 1);
        account.setType(AccountType.CHECKING);
        account.setBalance(account.getBalance().add(new BigDecimal(1)));
        assertFalse(this.account.equals(account));

        accountDao.updateAccount(account);

        final Account updatedAccount = accountDao.getAccount(id);
        assertFalse(account == updatedAccount);
        assertFalse(this.account.equals(updatedAccount));
        assertEquals(account, updatedAccount);
    }
}
