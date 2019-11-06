package com.parasoft.parabank.dao;

import java.util.ArrayList;
import java.util.List;

import com.parasoft.parabank.domain.Account;

public class InMemoryAccountDao implements AccountDao {
    private static int ID = 0;

    private final List<Account> accounts;

    public InMemoryAccountDao(List<Account> accounts) {
        this.accounts = accounts;
        ID = accounts.size();
    }

    @Override
    public Account getAccount(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }

        return null;
    }

    @Override
    public List<Account> getAccountsForCustomerId(int customerId) {
        List<Account> customerAccounts = new ArrayList<>();

        for (Account account : accounts) {
            if (account.getCustomerId() == customerId) {
                customerAccounts.add(account);
            }
        }

        return customerAccounts;
    }

    @Override
    public int createAccount(Account account) {
        account.setId(++ID);
        accounts.add(account);
        return ID;
    }

    @Override
    public void updateAccount(Account account) {
        for (Account existingAccount : accounts) {
            if (existingAccount.getId() == account.getId()) {
                accounts.remove(existingAccount);
                accounts.add(account);
                break;
            }
        }
    }
}
