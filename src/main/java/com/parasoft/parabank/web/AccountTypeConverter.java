package com.parasoft.parabank.web;

import org.springframework.core.convert.converter.Converter;

import com.parasoft.parabank.domain.Account.AccountType;

/**
 * Class for converting a string (from an input form) to an AccountType
 */
public class AccountTypeConverter implements Converter<String, AccountType> {
    @Override
    public AccountType convert(String key) {
        return key == null ? null : AccountType.valueOf(key);
    }
}
