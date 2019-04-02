package com.parasoft.parabank.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.BillPayResult;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Payee;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;
import com.parasoft.parabank.domain.TransactionCriteria.SearchType;
import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.domain.logic.BankManager;
import com.parasoft.parabank.util.DateTimeAdapter;

// import java.text.DateFormat;

/*
 * ParaBank web service implementation
 */
@WebService(endpointInterface = "com.parasoft.parabank.service.ParaBankService", serviceName = "ParaBank")
public class ParaBankServiceImpl implements ParaBankService, AdminManagerAware, BankManagerAware {
    private static final Logger log = LoggerFactory.getLogger(ParaBankServiceImpl.class);

    private AdminManager adminManager;

    private BankManager bankManager;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#buyPosition(int, int, java.lang.String, java.lang.String,
     * int, java.math.BigDecimal)
     */
    @Override
    public List<Position> buyPosition(final int customerId, final int accountId, final String name, final String symbol,
        final int shares, final BigDecimal pricePerShare) throws ParaBankServiceException {
        try {
            return bankManager.buyPosition(customerId, accountId, name, symbol, shares, pricePerShare);
        } catch (final DataAccessException e) {
            log.error("{} caught :", e.getClass().getSimpleName(), e);
            throw new ParaBankServiceException(
                "Could not buy position, Customer ID:" + customerId + ", Account ID:" + accountId + ", Company Name:"
                    + name + ", Company Symbol:" + symbol + ", Shares:" + shares + ", Price:" + pricePerShare,
                e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#cleanDB()
     */
    @Override
    public void cleanDB() {
        adminManager.cleanDB();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#createAccount(int, java.math.BigDecimal, java.math.BigDecimal,
     * int)
     */
    @Override
    public Account createAccount(final int customerId, final int newAccountType, final int fromAccountId)
            throws ParaBankServiceException {
        Account newAccount = new Account();

        try {
            newAccount = new Account();
            newAccount.setCustomerId(customerId);
            newAccount.setBalance(BigDecimal.ZERO);
            newAccount.setType(newAccountType);
            newAccount.setId(bankManager.createAccount(newAccount, fromAccountId));
            return newAccount;
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException(
                "Could not create new account for customer " + customerId + " from account " + fromAccountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#deposit(int, java.math.BigDecimal)
     */
    @Override
    public String deposit(final int accountId, final BigDecimal amount) throws ParaBankServiceException {
        try {
            bankManager.deposit(accountId, amount, "Deposit via Web Service");
            return "Successfully deposited $" + amount + " to account #" + accountId;
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find account number " + accountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getBalance(int)
     */
    @Override
    public Account getAccount(final int accountId) throws ParaBankServiceException {
        try {
            return bankManager.getAccount(accountId);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find account #" + accountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getAccounts(int)
     */
    @Override
    public List<Account> getAccounts(final int customerId) throws ParaBankServiceException {
        try {
            final Customer customer = bankManager.getCustomer(customerId);
            List<Account> accountsForCustomer = bankManager.getAccountsForCustomer(customer);
            Collections.sort(accountsForCustomer, Comparator.comparingInt(Account::getId));
            return accountsForCustomer;
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find customer #" + customerId, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public AdminManager getAdminManager() {
        return adminManager;
    }

    /** {@inheritDoc} */
    @Override
    public BankManager getBankManager() {
        return bankManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getCustomer(int)
     */
    @Override
    public Customer getCustomer(final int customerId) throws ParaBankServiceException {
        try {
            return bankManager.getCustomer(customerId);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find customer #" + customerId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getPosition(int)
     */
    @Override
    public Position getPosition(final int positionId) throws ParaBankServiceException {
        try {
            return bankManager.getPosition(positionId);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find position #" + positionId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getPositionHistory(int, java.util.Date, java.util.Date)
     */
    @Override
    public List<HistoryPoint> getPositionHistory(final int positionId, final String startDate, final String endDate)
            throws ParaBankServiceException {
        try {
            return bankManager.getPositionHistory(positionId, DateTimeAdapter.dateFromString(startDate),
                    DateTimeAdapter.dateFromString(endDate));
        } catch (final Exception e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find position #" + positionId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getPositions(int)
     */
    @Override
    public List<Position> getPositions(final int customerId) throws ParaBankServiceException {
        try {
            final Customer customer = bankManager.getCustomer(customerId);
            return bankManager.getPositionsForCustomer(customer);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find customer #" + customerId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransaction(int)
     */
    @Override
    public Transaction getTransaction(final int transactionId) throws ParaBankServiceException {
        try {
            return bankManager.getTransaction(transactionId);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find transaction #" + transactionId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransactions(int)
     */
    @Override
    public List<Transaction> getTransactions(final int accountId) throws ParaBankServiceException {
        try {
            final Account account = bankManager.getAccount(accountId);
            return bankManager.getTransactionsForAccount(account);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find transactions for account #" + accountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransactions(int, java.math.BigDecimal)
     */
    @Override
    public List<Transaction> getTransactionsByAmount(final int accountId, final BigDecimal amount)
            throws ParaBankServiceException {

        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setAmount(amount);
        criteria.setSearchType(SearchType.AMOUNT);
        return bankManager.getTransactionsForAccount(accountId, criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransactionsByMonthAndType(int, java.lang.String,
     * java.lang.String)
     */
    @Override
    public List<Transaction> getTransactionsByMonthAndType(final int accountId, final String month, final String type)
            throws ParaBankServiceException {

        final TransactionCriteria criteria = new TransactionCriteria();
        criteria.setSearchType(SearchType.ACTIVITY);
        criteria.setTransactionType(type);
        criteria.setMonth(month);
        return bankManager.getTransactionsForAccount(accountId, criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransactions(int, java.lang.String, java.lang.String)
     */
    @Override
    public List<Transaction> getTransactionsByToFromDate(final int accountId, final String fromDate,
        final String toDate) throws ParaBankServiceException {

        final TransactionCriteria criteria = new TransactionCriteria();

        try {
            criteria.setFromDate(TransactionCriteria.DATE_FORMATTER.get().parse(fromDate));
        } catch (final ParseException e) {
            log.error("ParseException caught :", e);
            throw new ParaBankServiceException("Unable to parse date " + fromDate, e);
        }

        try {
            criteria.setToDate(TransactionCriteria.DATE_FORMATTER.get().parse(toDate));
        } catch (final ParseException e) {
            log.error("ParseException caught :", e);
            throw new ParaBankServiceException("Unable to parse date " + toDate, e);
        }

        criteria.setSearchType(SearchType.DATE_RANGE);
        return bankManager.getTransactionsForAccount(accountId, criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#getTransactionsOnDate(int, java.lang.String)
     */
    @Override
    public List<Transaction> getTransactionsOnDate(final int accountId, final String onDate)
            throws ParaBankServiceException {

        final TransactionCriteria criteria = new TransactionCriteria();
        try {
            criteria.setOnDate(TransactionCriteria.DATE_FORMATTER.get().parse(onDate));
        } catch (final ParseException e) {
            log.error("ParseException caught :", e);
            throw new ParaBankServiceException("Unable to parse date " + onDate, e);
        }

        criteria.setSearchType(SearchType.DATE);
        return bankManager.getTransactionsForAccount(accountId, criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#initializeDB()
     */
    @Override
    public void initializeDB() {
        adminManager.initializeDB();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#login(java.lang.String, java.lang.String)
     */
    @Override
    public Customer login(final String username, final String password) throws ParaBankServiceException {
        final Customer customer = bankManager.getCustomer(username, password);
        if (customer == null) {
            throw new ParaBankServiceException("Invalid username and/or password");
        }
        return customer;//.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#requestLoan(int, java.math.BigDecimal, java.math.BigDecimal,
     * int)
     */
    @Override
    public LoanResponse requestLoan(final int customerId, final BigDecimal amount, final BigDecimal downPayment,
        final int fromAccountId) throws ParaBankServiceException {
        try {
            return bankManager.requestLoan(customerId, amount, downPayment, fromAccountId);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find account #" + fromAccountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.BankManager#sellPosition(int, int, int, int, java.math.BigDecimal)
     */
    @Override
    public List<Position> sellPosition(final int customerId, final int accountId, final int positionId,
        final int shares, final BigDecimal pricePerShare) throws ParaBankServiceException {
        try {
            return bankManager.sellPosition(customerId, accountId, positionId, shares, pricePerShare);
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not sell position, Customer ID:" + customerId + ", Account ID:"
                + accountId + ", Position ID:" + positionId + ", Shares:" + shares + ", Price:" + pricePerShare, e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    /** {@inheritDoc} */
    @Override
    public void setBankManager(final BankManager bankManager) {
        this.bankManager = bankManager;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#setParameter(java.lang.String, java.lang.String)
     */
    @Override
    public void setParameter(final String name, final String value) {
        adminManager.setParameter(name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#shutdownJmsListener()
     */
    @Override
    public void shutdownJmsListener() {
        adminManager.shutdownJmsListener();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#startupJmsListener()
     */
    @Override
    public void startupJmsListener() {
        adminManager.startupJmsListener();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#transfer(int, int, java.math.BigDecimal)
     */

    @Override
    public String transfer(final int fromAccountId, final int toAccountId, final BigDecimal amount)
            throws ParaBankServiceException {
        try {
            bankManager.transfer(fromAccountId, toAccountId, amount);
            return "Successfully transferred $" + amount + " from account #" + fromAccountId + " to account #"
                + toAccountId;
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException(
                "Could not find account number " + fromAccountId + " and/or " + toAccountId, e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#updateCustomer(int, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String updateCustomer(final int customerId, final String firstName, final String lastName,
        final String street, final String city, final String state, final String zipCode, final String phoneNumber,
        final String ssn, final String username, final String password) throws ParaBankServiceException {
        try {
            final Address customerAddress = new Address();
            customerAddress.setStreet(URLDecoder.decode(street, "UTF-8"));
            customerAddress.setCity(URLDecoder.decode(city, "UTF-8"));
            customerAddress.setState(URLDecoder.decode(state, "UTF-8"));
            customerAddress.setZipCode(URLDecoder.decode(zipCode, "UTF-8"));
            final Customer updatedCustomer = new Customer();
            updatedCustomer.setAddress(customerAddress);
            updatedCustomer.setFirstName(URLDecoder.decode(firstName, "UTF-8"));
            updatedCustomer.setLastName(URLDecoder.decode(lastName, "UTF-8"));
            updatedCustomer.setId(customerId);
            updatedCustomer.setPhoneNumber(URLDecoder.decode(phoneNumber, "UTF-8"));
            updatedCustomer.setSsn(ssn);
            updatedCustomer.setUsername(username);
            updatedCustomer.setPassword(password);
            bankManager.updateCustomer(updatedCustomer);
        } catch (final UnsupportedEncodingException e) {
            throw new ParaBankServiceException("Unsupported encoding"); // parasoft-suppress EXCEPT.CTE "No need for chained exception in this case per higher level design. Reviewed and found appropriate."
        }
        return "Successfully updated customer profile";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.service.ParaBankService#withdraw(int, java.math.BigDecimal)
     */
    @Override
    public String withdraw(final int accountId, final BigDecimal amount) throws ParaBankServiceException {
        try {
            bankManager.withdraw(accountId, amount, "Withdraw via Web Service");
            return "Successfully withdrew $" + amount + " from account #" + accountId;
        } catch (final DataAccessException e) {
            log.error("DataAccessException caught :", e);
            throw new ParaBankServiceException("Could not find account number " + accountId, e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.parasoft.parabank.service.ParaBankService#billPay(int, java.math.BigDecimal, com.parasoft.parabank.domain.Payee)
     */
    @Override
    public BillPayResult billPay(int accountId, BigDecimal amount, Payee payee) throws ParaBankServiceException {
        bankManager.withdraw(accountId, amount, String.format("Bill Payment to %s", payee.getName()));
        return new BillPayResult(accountId, amount, payee.getName());
    }
}
