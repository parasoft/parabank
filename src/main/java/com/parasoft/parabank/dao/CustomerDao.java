package com.parasoft.parabank.dao;

import com.parasoft.parabank.domain.Customer;

/**
 * Interface for accessing Customer information from a data source
 */
public interface CustomerDao {

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
     * Retrieve a specific bank customer by usernamd and password
     * Used by the customer lookup mechanism
     *
     * @param ssn the customer's social security number
     * @return Customer object representing the bank customer
     */
    Customer getCustomer(String ssn);

    /**
     * Add a new customer to the data source
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
}
