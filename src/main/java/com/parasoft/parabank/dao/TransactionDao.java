package com.parasoft.parabank.dao;

import java.util.List;

import com.parasoft.parabank.domain.Transaction;
import com.parasoft.parabank.domain.TransactionCriteria;

/**
 * Interface for accessing Transaction information from a data source
 */
public interface TransactionDao {

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
    List<Transaction> getTransactionsForAccount(int accountId);

    /**
     * Retrieve transactions for a given account matching given criteria
     *
     * @param accountId the account id to lookup
     * @param criteria set of criteria that the retrieved transactions must adhere to
     * @return list of matching transactions for the given account
     */
    List<Transaction> getTransactionsForAccount(int accountId, TransactionCriteria criteria);

    /**
     * Add a new transaction to the data source
     *
     * Note that the transaction id will be automatically generated
     *
     * @param transaction the transaction information to store
     * @return generated transaction id
     */
    int createTransaction(Transaction transaction);
}
