package com.parasoft.parabank.web.form;

import com.parasoft.parabank.domain.Account.AccountType;

/**
 * Backing class for account creation form
 */
public class OpenAccountForm {
    private AccountType type;
    private int fromAccountId;

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public int getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(int fromAccountId) {
        this.fromAccountId = fromAccountId;
    }
}
