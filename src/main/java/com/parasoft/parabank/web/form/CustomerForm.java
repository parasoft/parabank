package com.parasoft.parabank.web.form;

import com.parasoft.parabank.domain.Customer;

/**
 * Backing class for customer registration form
 */
public class CustomerForm {
    private final Customer customer;

    private String repeatedPassword;

    public CustomerForm(Customer customer) {
        this.customer = customer;
    }

    public CustomerForm() {
        customer = new Customer();
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}
