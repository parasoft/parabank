package com.parasoft.parabank.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.parasoft.parabank.dao.jdbc.internal.StockDataInserter;
import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;
import com.parasoft.parabank.util.DateTimeAdapter;

/**
 * @req PAR-43
 *
 */
@Transactional
public class ParaBankServiceImplTest extends AbstractParaBankDataSourceTest {
    private static final int CUSTOMER_ID = 12212;

    private static final int ACCOUNT1_ID = 12345;

    private static final int ACCOUNT2_ID = 54321;

    private static final int TRANSACTION_ID = 13033;

    private static final int POSITION_ID = 12345;

    private static final BigDecimal ONE_HUNDRED_DOLLARS = new BigDecimal("100.00");

    private static final String NAME = "Test Company";

    private static final String SYMBOL = "TC";

    @Resource(name = "paraBankService")
    private ParaBankService paraBankService;

    @Resource(name = "stockDataInserter")
    private StockDataInserter stockDataInserter;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        if (stockDataInserter.getDataCount() < 1) {
            stockDataInserter.insertData();
        }
    }

    public void setParaBankService(final ParaBankService paraBankService) {
        this.paraBankService = paraBankService;
    }

    public void setStockDataInserter(final StockDataInserter stockDataInserter) {
        this.stockDataInserter = stockDataInserter;
    }

    @Test
    @Transactional
    @Rollback
    public void testBuyAndSellPosition() throws Exception {
        Position position = paraBankService.getPosition(POSITION_ID);
        assertEquals(POSITION_ID, position.getPositionId());
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(new BigDecimal("23.53").floatValue(), position.getPurchasePrice().floatValue(), 0.0001f);
        final BigDecimal cost = position.getPurchasePrice();

        Account account = paraBankService.getAccount(ACCOUNT2_ID);
        assertEquals(ACCOUNT2_ID, account.getId());
        BigDecimal balance = account.getAvailableBalance();

        final List<Position> currentList = paraBankService.getPositions(CUSTOMER_ID);
        final int size = currentList.size();
        final int[] positionIds = new int[size];
        int iterator = 0;
        for (final Position pos : currentList) {
            positionIds[iterator] = pos.getPositionId();
            iterator++;
        }

        // position = paraBankService.buyPosition(CUSTOMER_ID, ACCOUNT2_ID,
        // NAME, SYMBOL, 1, cost);
        List<Position> positions = paraBankService.buyPosition(CUSTOMER_ID, ACCOUNT2_ID, NAME, SYMBOL, 1, cost);
        assertEquals(size + 1, positions.size());

        account = paraBankService.getAccount(ACCOUNT2_ID);
        assertEquals(ACCOUNT2_ID, account.getId());
        balance = balance.subtract(cost);
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(balance.floatValue(), account.getAvailableBalance().floatValue(), 0.0001f);
        // final long same = balance.compareTo(account.getAvailableBalance());
        // String.format("expected: <%1$.2f> but actual: <%2$.2f>", balance,
        // account.getAvailableBalance());
        // assertEquals(same, 0L); // had to replace due to a weird difference
        // in
        // precision

        // int positionId = position.getPositionId();
        int newPositionId = -1;
        for (final Position pos : positions) {
            boolean found = false;
            final int id = pos.getPositionId();
            for (int positionId : positionIds) {
                if (id == positionId) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                newPositionId = id;
            }
        }

        position = paraBankService.getPosition(newPositionId);
        assertNotNull(position.getPositionId());
        assertEquals(cost, position.getCostBasis());

        positions = paraBankService.sellPosition(CUSTOMER_ID, ACCOUNT2_ID, newPositionId, 1, cost);
        assertEquals(size, positions.size());

        account = paraBankService.getAccount(ACCOUNT2_ID);
        assertEquals(ACCOUNT2_ID, account.getId());
        balance = balance.add(cost);
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(balance.floatValue(), account.getAvailableBalance().floatValue(), 0.0001f);

        try {
            paraBankService.getPosition(newPositionId);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    @Transactional
    @Rollback
    public void testDeposit() throws Exception {
        final String message = paraBankService.deposit(ACCOUNT1_ID, ONE_HUNDRED_DOLLARS);
        assertEquals("Successfully deposited $" + ONE_HUNDRED_DOLLARS + " to account #" + ACCOUNT1_ID, message);

        try {
            paraBankService.deposit(-1, ONE_HUNDRED_DOLLARS);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetAccount() throws Exception {
        final Account account = paraBankService.getAccount(ACCOUNT1_ID);
        assertEquals(ACCOUNT1_ID, account.getId());
        assertEquals(new BigDecimal("-2300.00").floatValue(), account.getBalance().floatValue(), 0.0001f);

        try {
            paraBankService.getAccount(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetAccounts() throws Exception {
        final List<Account> accounts = paraBankService.getAccounts(CUSTOMER_ID);
        assertEquals(11, accounts.size());

        try {
            paraBankService.getAccounts(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetCustomer() throws Exception {
        final Customer customer = paraBankService.getCustomer(CUSTOMER_ID);
        assertEquals(CUSTOMER_ID, customer.getId());
        assertEquals("John Smith", customer.getFullName());

        try {
            paraBankService.getCustomer(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetPosition() throws Exception {
        final Position position = paraBankService.getPosition(POSITION_ID);
        assertEquals(POSITION_ID, position.getPositionId());
        assertEquals(new BigDecimal("23.53").floatValue(), position.getPurchasePrice().floatValue(), 0.0001f);

        try {
            paraBankService.getPosition(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetPositionHistory() throws Exception {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final String endDate = DateTimeAdapter.OUTPUT_FORMAT.format(calendar.toInstant());
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        final String startDate = DateTimeAdapter.OUTPUT_FORMAT.format(calendar.toInstant());
        final List<HistoryPoint> history = paraBankService.getPositionHistory(POSITION_ID, startDate, endDate);
        assertThat(history.size(), allOf(greaterThanOrEqualTo(10), lessThanOrEqualTo(12)));
        try {
            paraBankService.getPositionHistory(-1, startDate, endDate);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetPositions() throws Exception {
        final List<Position> positions = paraBankService.getPositions(CUSTOMER_ID);
        assertEquals(3, positions.size());

        try {
            paraBankService.getPositions(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetTransaction() throws Exception {
        final Transaction transaction = paraBankService.getTransaction(TRANSACTION_ID);
        assertEquals(TRANSACTION_ID, transaction.getId());
        assertEquals(ONE_HUNDRED_DOLLARS, transaction.getAmount());

        try {
            paraBankService.getTransaction(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testGetTransactions() throws Exception {
        final List<Transaction> transactions = paraBankService.getTransactions(ACCOUNT1_ID);
        assertEquals(7, transactions.size());

        try {
            paraBankService.getTransactions(-1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testLogin() throws Exception {
        assertEquals(12212, paraBankService.login("john", "demo").getId());

        try {
            paraBankService.login(null, null);
            paraBankService.login("", null);
            paraBankService.login(null, "");
            paraBankService.login("", "");
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testRequestLoan() throws Exception {
        final LoanResponse response =
            paraBankService.requestLoan(CUSTOMER_ID, ONE_HUNDRED_DOLLARS, ONE_HUNDRED_DOLLARS, ACCOUNT1_ID);
        assertTrue(response.isApproved());
        assertNotNull(response.getResponseDate());

        try {
            paraBankService.requestLoan(-1, ONE_HUNDRED_DOLLARS, ONE_HUNDRED_DOLLARS, ACCOUNT1_ID);
            paraBankService.requestLoan(CUSTOMER_ID, ONE_HUNDRED_DOLLARS, ONE_HUNDRED_DOLLARS, -1);
            paraBankService.requestLoan(-1, ONE_HUNDRED_DOLLARS, ONE_HUNDRED_DOLLARS, -1);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testTransfer() throws Exception {
        final String message = paraBankService.transfer(ACCOUNT1_ID, ACCOUNT2_ID, ONE_HUNDRED_DOLLARS);
        assertEquals("Successfully transferred $" + ONE_HUNDRED_DOLLARS + " from account #" + ACCOUNT1_ID
            + " to account #" + ACCOUNT2_ID, message);

        try {
            paraBankService.transfer(-1, ACCOUNT2_ID, ONE_HUNDRED_DOLLARS);
            paraBankService.transfer(ACCOUNT1_ID, -1, ONE_HUNDRED_DOLLARS);
            paraBankService.transfer(-1, -1, ONE_HUNDRED_DOLLARS);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }

    @Test
    public void testWithdraw() throws Exception {
        final String message = paraBankService.withdraw(ACCOUNT1_ID, ONE_HUNDRED_DOLLARS);
        assertEquals("Successfully withdrew $" + ONE_HUNDRED_DOLLARS + " from account #" + ACCOUNT1_ID, message);

        try {
            paraBankService.withdraw(-1, ONE_HUNDRED_DOLLARS);
            fail("Did not throw expected ParaBankServiceException");
        } catch (final ParaBankServiceException e) {
        }
    }
}
