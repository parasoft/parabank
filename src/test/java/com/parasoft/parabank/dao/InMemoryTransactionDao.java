package com.parasoft.parabank.dao;

import java.util.*;

import com.parasoft.parabank.domain.*;

public class InMemoryTransactionDao implements TransactionDao {
    private static int ID = 0;

    private List<Transaction> transactions;

    public InMemoryTransactionDao() {
        this(new ArrayList<Transaction>());
    }

    public InMemoryTransactionDao(List<Transaction> transactions) {
        this.transactions = transactions;
        ID = transactions.size();
    }

    @Override
    public Transaction getTransaction(int id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }

        return null;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(int accountId) {
        List<Transaction> accountTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : transactions) {
            if (transaction.getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }

        return accountTransactions;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(int accountId,
            TransactionCriteria criteria) {
        List<Transaction> accountTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : transactions) {
            if (transaction.getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }

        return accountTransactions;
    }

    @Override
    public int createTransaction(Transaction transaction) {
        transaction.setId(++ID);
        transactions.add(transaction);
        return ID;
    }
}
