package com.parasoft.parabank.web;

import com.parasoft.parabank.domain.Customer;

/**
 * Object for storing logged in user session data
 */
public class UserSession {
    private final Customer customer;

    public UserSession(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
