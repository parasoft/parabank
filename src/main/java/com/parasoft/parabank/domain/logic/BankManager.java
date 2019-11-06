package com.parasoft.parabank.domain.logic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.parasoft.parabank.domain.Account;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.LoanResponse;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;

/**
 * Interface that provides the core bank business operations
 */
public interface BankManager {

    /**
     * Retrieve a specific bank customer by id
     *
     * @param id the customer id to retrieve
     * @return Customer object representing the bank customer
     */
    Customer getCustomer(int id);

    /**
     * Retrieve a specific bank customer by username and password
     * Used by the login mechanism
     *
     * @param username the customer's username
     * @param password the customer's password
     * @return Customer object representing the bank customer
     */
    Customer getCustomer(String username, String password);

    /**
     * Retrieve a specific bank customer by username and password
     * Used by the customer lookup mechanism
     *
     * @param ssn the customer's social security number
     * @return Customer object representing the bank customer
     */
    Customer getCustomer(String ssn);

    /**
     * Add a new customer to the system
     *
     * Note that the customer id will be automatically generated
     *
     * @param customer the customer information to store
     * @return generated customer id
     */
    int createCustomer(Customer customer);

    /**
     * Update stored information for a given customer
     *
     * @param customer the customer to update
     */
    void updateCustomer(Customer customer);

    /**
     * Buy a new position and add it to the system
     *
     * Note that the position id will be automatically generated
     *
     * @param customerId the customer id of the purchaser
     * @param accountId the account id to pay from
     * @param name the name of the stock company
     * @param symbol the stock company symbol
     * @param shares the number of shares to purchase
     * @param pricePerShare the cost per share purchased
     * @return a list of positions owned by the customer
     */
    List<Position> buyPosition(int customerId, int accountId, String name, String symbol, int shares, BigDecimal pricePerShare);

    /**
     * Sell a position and update or delete it from the system
     *
     * @param customerId the customer id of the seller
     * @param accountId the account id to deposit to
     * @param positionId the position id of the position being sold
     * @param shares the number of shares to sell
     * @param pricePerShare the current price per share
     * @return a list of positions owned by the customer
     */
    List<Position> sellPosition(int customerId, int accountId, int positionId, int shares, BigDecimal pricePerShare);

    /**
     * Retrieve all positions for a given customer
     *
     * @param customer the customer to lookup
     * @return list of positions for the given customer
     */
    List<Position> getPositionsForCustomer(Customer customer);

    /**
     * Retrieve a specific position by id
     *
     * @param positionId the position id to retrieve
     * @return Position object representing the position
     */
    Position getPosition(int positionId);

    /**
     * Return position history for a given position id
     * and date range
     *
     * @param positionId the position id
     * @param startDate the start date in the date range
     * @param endDate the end date in the date range
     * @return a list of history points
     */
    List<HistoryPoint> getPositionHistory(int positionId, Date startDate, Date endDate);

    /**
     * Create a new position
     *
     * @param customerId the customer ID
     * @param name the company name
     * @param symbol the company symbol
     * @param shares the number of shares
     * @param pricePerShare the cost per share of stock
     * @return the newly created position
     */
    Position createPosition(int customerId, String name, String symbol, int shares, BigDecimal pricePerShare);

    /**
     * Update a position
     *
     * @param position the position data to update
     * @return success
     */
    boolean updatePosition(Position position);

    /**
     * Delete a position
     *
     * @param position the position data to delete
     * @return success
     */
    boolean deletePosition(Position position);

    /**
     * Retrieve a specific bank account by id
     *
     * @param id the account id to retrieve
     * @return Account object representing the bank account
     */
    Account getAccount(int id);

    /**
     * Retrieve all accounts for a given customer
     *
     * @param customer the customer to lookup
     * @return list of accounts belonging to the given customer
     */
    List<Account> getAccountsForCustomer(Customer customer);

    /**
     * Add a new account to the system, withdrawing funds from a seed account
     *
     * Note that the account id will be automatically generated
     *
     * @param account the account information to store
     * @param fromAccountId the account id to seed from
     * @return generated account id
     */
    int createAccount(Account account, int fromAccountId);

    /**
     * Retrieve a specific account transaction by id
     *
     * @param id the transaction id to retrieve
     * @return Transaction object representing the account transaction
     */
    Transaction getTransaction(int id);

    /**
     * Retrieve all transactions for a given account
     *
     * @param accountId the account id to lookup
     * @return list of transactions for the given account
     */
    List<Transaction> getTransactionsForAccount(Account account);

    /**
     * Retrieve transactions for a given account matching given criteria
     *
     * @param accountId the account id to lookup
     * @param criteria set of criteria that the retrieved transactions must adhere to
     * @return list of matching transactions for the given account
     */
    List<Transaction> getTransactionsForAccount(int accountId, TransactionCriteria criteria);

    /**
     * Transfer funds between two accounts
     *
     * @param fromAccountId the account from which to withdraw money
     * @param toAccountId the account to which to deposit money
     * @param amount the amount of money to transfer
     */
    void transfer(int fromAccountId, int toAccountId, BigDecimal amount);

    /**
     * Withdraw funds from an account
     *
     * @param accountId the account from which to withdraw money
     * @param amount the amount of money to withdraw
     * @param description a description of the transaction
     */
    Transaction deposit(int accountId, BigDecimal amount, String description);

    /**
     * Deposit funds to an account
     *
     * @param accountId the account to which to deposit money
     * @param amount the amount of money to deposit
     * @param description a description of the transaction
     */
    void withdraw(int accountId, BigDecimal amount, String description);

    /**
     * Request a loan
     *
     * @param customerId the customer requesting a loan
     * @param amount the amount of the loan
     * @param downPayment the down payment for the loan
     * @param fromAccountId the account from which to deduct the down payment
     * @return response the result of the loan request
     */
    LoanResponse requestLoan(int customerId, BigDecimal amount,
            BigDecimal downPayment, int fromAccountId);
}
