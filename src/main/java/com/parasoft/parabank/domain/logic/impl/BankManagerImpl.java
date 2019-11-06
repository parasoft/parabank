package com.parasoft.parabank.domain.logic.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parasoft.parabank.dao.AccountDao;
import com.parasoft.parabank.dao.AdminDao;
import com.parasoft.parabank.dao.CustomerDao;
import com.parasoft.parabank.dao.PositionDao;
import com.parasoft.parabank.dao.TransactionDao;
import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Account.AccountType;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.LoanRequest;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.Transaction.TransactionType;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.logic.AdminParameters;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.domain.logic.LoanProvider;

public class BankManagerImpl implements BankManager {
    private static final Logger log = LoggerFactory.getLogger(BankManagerImpl.class);

    private AccountDao accountDao;

    private CustomerDao customerDao;

    private PositionDao positionDao;

    private TransactionDao transactionDao;

    private AdminDao adminDao;

    private LoanProvider loanProvider;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#buyPosition(int, int, java.lang.String, java.lang.String,
     * int, java.math.BigDecimal)
     */
    @Override
    public List<Position> buyPosition(final int customerId, final int accountId, final String name, final String symbol,
        final int shares, final BigDecimal pricePerShare) {
        withdraw(accountId, pricePerShare.multiply(new BigDecimal(shares)), "Funds Transfer Sent");
        log.info("Withdrew funds for Stock Purchase");

        final Position position = createPosition(customerId, name, symbol, shares, pricePerShare);
        final int positionId = positionDao.createPosition(position);
        log.info("Created position with id = " + positionId + " with " + shares + " shares");

        final Customer customer = getCustomer(customerId);

        return getPositionsForCustomer(customer);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#createAccount(com.parasoft.parabank.domain.Account)
     */
    @Override
    public int createAccount(final Account account, final int fromAccountId) {
        final int id = accountDao.createAccount(account);

        transfer(fromAccountId, id, new BigDecimal(adminDao.getParameter(AdminParameters.MINIMUM_BALANCE)));

        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#createCustomer(com.parasoft.parabank.domain.Customer)
     */
    @Override
    public int createCustomer(final Customer customer) {
        final int id = customerDao.createCustomer(customer);
        log.info("Created customer with id = " + id);
        final Account account = new Account();
        account.setCustomerId(id);
        account.setType(AccountType.CHECKING);
        account.setBalance(new BigDecimal(adminDao.getParameter(AdminParameters.INITIAL_BALANCE)));
        accountDao.createAccount(account);
        log.info("Created new account with id = " + account.getId() + " and balance " + account.getBalance()
            + " for customer with id = " + id);
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#createPosition(com.parasoft.parabank.domain.Position)
     */
    @Override
    public Position createPosition(final int customerId, final String name, final String symbol, final int shares,
        final BigDecimal pricePerShare) {
        final Position position = new Position();
        position.setCustomerId(customerId);
        position.setName(name);
        position.setSymbol(symbol);
        position.setShares(shares);
        position.setPurchasePrice(pricePerShare);
        return position;
    }

    private Transaction createTransaction(final Account account, final TransactionType type, final Date date,
        final BigDecimal amount, final String description) {
        final Transaction transaction = new Transaction();
        transaction.setAccountId(account.getId());
        transaction.setType(type);
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        return transaction;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#deletePosition(com.parasoft.parabank.domain.Position)
     */
    @Override
    public boolean deletePosition(final Position position) {
        return positionDao.deletePosition(position);
    }

    /* ========================= Customer Methods ========================= */

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#deposit(int, java.math.BigDecimal, java.lang.String)
     */
    @Override
    public Transaction deposit(final int accountId, final BigDecimal amount, final String description) {
        final Account account = accountDao.getAccount(accountId);

        account.credit(amount);
        accountDao.updateAccount(account);
        log.info("Credited account with id = " + accountId + " in the amount of " + amount);

        final Transaction transaction =
            createTransaction(account, TransactionType.Credit, new Date(), amount, description);
        transactionDao.createTransaction(transaction);
        log.info("Created credit transaction with id = " + transaction.getId() + " with description: " + description);
        return transaction;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getAccount(int)
     */
    @Override
    public Account getAccount(final int id) {
        return accountDao.getAccount(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getAccountsForCustomer(com.parasoft.parabank.domain.Customer)
     */
    @Override
    public List<Account> getAccountsForCustomer(final Customer customer) {
        return accountDao.getAccountsForCustomerId(customer.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getCustomer(int)
     */
    @Override
    public Customer getCustomer(final int id) {
        return customerDao.getCustomer(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getCustomer(java.lang.String)
     */
    @Override
    public Customer getCustomer(final String ssn) {
        return customerDao.getCustomer(ssn);
    }

    /* ========================= Account Methods ========================= */

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getCustomer(java.lang.String, java.lang.String)
     */
    @Override
    public Customer getCustomer(final String username, final String password) {
        return customerDao.getCustomer(username, password);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getPosition(int)
     */
    @Override
    public Position getPosition(final int positionId) {
        return positionDao.getPosition(positionId);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getPositionHistory(int, java.util.Date, java.util.Date)
     */
    @Override
    public List<HistoryPoint> getPositionHistory(final int positionId, final Date startDate, final Date endDate) {
        return positionDao.getPositionHistory(positionId, startDate, endDate);
    }

    /* ========================= Position Methods ========================= */

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getPosition(int)
     */
    @Override
    public List<Position> getPositionsForCustomer(final Customer customer) {
        return positionDao.getPositionsForCustomerId(customer.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#getTransaction(int)
     */
    @Override
    public Transaction getTransaction(final int id) {
        return transactionDao.getTransaction(id);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.parasoft.parabank.domain.logic.BankManager#getTransactionsForAccount(com.parasoft.parabank.domain.Account)
     */
    @Override
    public List<Transaction> getTransactionsForAccount(final Account account) {
        return transactionDao.getTransactionsForAccount(account.getId());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.parasoft.parabank.domain.logic.BankManager#getTransactionsForAccount(com.parasoft.parabank.domain.Account,
     * com.parasoft.parabank.domain.TransactionCriteria)
     */
    @Override
    public List<Transaction> getTransactionsForAccount(final int accountId, final TransactionCriteria criteria) {
        return transactionDao.getTransactionsForAccount(accountId, criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#requestLoan(int, java.math.BigDecimal, java.math.BigDecimal,
     * int)
     */
    @Override
    public LoanResponse requestLoan(final int customerId, final BigDecimal amount, final BigDecimal downPayment,
        final int fromAccountId) {
        final List<Account> accounts = accountDao.getAccountsForCustomerId(customerId);
        BigDecimal availableFunds = BigDecimal.ZERO;
        for (final Account account : accounts) {
            if (account.getType() != AccountType.LOAN) {
                availableFunds = availableFunds.add(account.getBalance());
            }
        }

        final LoanRequest loanRequest = new LoanRequest();
        loanRequest.setRequestDate(new Date());
        loanRequest.setCustomerId(customerId);
        loanRequest.setAvailableFunds(availableFunds);
        loanRequest.setDownPayment(downPayment);
        loanRequest.setLoanAmount(amount);

        log.info("Submitting loan request for customer with id = " + customerId + " in the amount of $" + amount
            + " at " + loanRequest.getRequestDate());
        final LoanResponse loanResponse = loanProvider.requestLoan(loanRequest);

        if (loanResponse.isApproved()) {
            final Account loanAccount = new Account();
            loanAccount.setCustomerId(customerId);
            loanAccount.setType(AccountType.LOAN);
            loanAccount.setBalance(amount);
            final int accountId = accountDao.createAccount(loanAccount);
            loanResponse.setAccountId(accountId);
            withdraw(fromAccountId, downPayment, "Down Payment for Loan # " + accountId);
        }

        return loanResponse;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#sellPosition(int, int, int, int, java.math.BigDecimal)
     */
    @Override
    public List<Position> sellPosition(final int customerId, final int accountId, final int positionId,
        final int shares, final BigDecimal pricePerShare) {
        final Position position = positionDao.getPosition(positionId);

        if (shares == position.getShares()) {
            deletePosition(position);
            log.info("Deleted position with id = " + position.getPositionId());
        } else {
            final int oldShares = position.getShares();
            position.setShares(oldShares - shares);
            updatePosition(position);
            log.info("Updated position with id = " + position.getPositionId() + ": new shares = "
                + (position.getShares() - shares));
        }
        deposit(accountId, pricePerShare.multiply(new BigDecimal(shares)), "Funds Transfer Received");
        log.info("Deposited funds from Stock Sale");
        final Customer customer = getCustomer(customerId);

        return getPositionsForCustomer(customer);
    }

    public void setAccountDao(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setAdminDao(final AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /* ========================= Transaction Methods ========================= */

    public void setCustomerDao(final CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void setLoanProvider(final LoanProvider loanProvider) {
        this.loanProvider = loanProvider;
    }

    public void setPositionDao(final PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public void setTransactionDao(final TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#transfer(int, int, java.math.BigDecimal)
     */
    @Override
    public void transfer(final int fromAccountId, final int toAccountId, final BigDecimal amount) {
        withdraw(fromAccountId, amount, "Funds Transfer Sent");
        deposit(toAccountId, amount, "Funds Transfer Received");
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#updateCustomer(com.parasoft.parabank.domain.Customer)
     */
    @Override
    public void updateCustomer(final Customer customer) {
        customerDao.updateCustomer(customer);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#updatePosition(com.parasoft.parabank.domain.Position)
     */
    @Override
    public boolean updatePosition(final Position position) {
        return positionDao.updatePosition(position);
    }

    /* ========================= Loan Methods ========================= */

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#withdraw(int, java.math.BigDecimal, java.lang.String)
     */
    @Override
    public void withdraw(final int accountId, final BigDecimal amount, final String description) {
        final Account account = accountDao.getAccount(accountId);

        account.debit(amount);
        accountDao.updateAccount(account);
        log.info("Debited account with id = " + accountId + " in the amount of " + amount);

        final Transaction transaction =
            createTransaction(account, TransactionType.Debit, new Date(), amount, description);
        transactionDao.createTransaction(transaction);
        log.info("Created debit transaction with id = " + transaction.getId() + " with description: " + description);
    }
}
