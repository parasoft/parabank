package com.parasoft.parabank.dao;

import java.util.List;

import com.parasoft.parabank.domain.Account;

/**
 * Interface for accessing Account information from a data source
 */
public interface AccountDao {

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
     * @param customerId the customer id to lookup
     * @return list of accounts belonging to the given customer
     */
    List<Account> getAccountsForCustomerId(int customerId);

    /**
     * Add a new account to the data source
     *
     * Note that the account id will be automatically generated
     *
     * @param account the account information to store
     * @return generated account id
     */
    int createAccount(Account account);

    /**
     * Update stored information for a given account
     *
     * @param account the account to update
     */
    void updateAccount(Account account);
}
