package com.parasoft.parabank.web;

import org.springframework.core.convert.converter.*;

import com.parasoft.parabank.domain.Account.*;

/**
 * Class for converting a string (from an input form) to an AccountType
 */
public class AccountTypeConverter implements Converter<String, AccountType> {
    @Override
    public AccountType convert(String key) {
        return key == null ? null : AccountType.valueOf(key);
    }
}
