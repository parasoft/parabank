package com.parasoft.parabank.dao;

import java.util.ArrayList;
import java.util.List;

import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.util.Util;

public class InMemoryCustomerDao implements CustomerDao {
    private static int ID = 0;

    private final List<Customer> customers;

    public InMemoryCustomerDao() {
        this(new ArrayList<Customer>());
    }

    public InMemoryCustomerDao(List<Customer> customers) {
        this.customers = customers;
        ID = customers.size();
    }

    @Override
    public Customer getCustomer(int id) {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }

        return null;
    }

    @Override
    public Customer getCustomer(String username, String password) {
        for (Customer customer : customers) {
            if (Util.equals(username, customer.getUsername()) &&
                    Util.equals(password, customer.getPassword())) {
                return customer;
            }
        }

        return null;
    }

    @Override
    public Customer getCustomer(String ssn) {
        for (Customer customer : customers) {
            if (Util.equals(ssn, customer.getSsn())) {
                return customer;
            }
        }

        return null;
    }

    @Override
    public int createCustomer(Customer customer) {
        customer.setId(++ID);
        customers.add(customer);
        return ID;
    }

    @Override
    public void updateCustomer(Customer customer) {
        for (Customer existingCustomer : customers) {
            if (existingCustomer.getId() == customer.getId()) {
                customers.remove(existingCustomer);
                customers.add(customer);
                break;
            }
        }
    }
}
