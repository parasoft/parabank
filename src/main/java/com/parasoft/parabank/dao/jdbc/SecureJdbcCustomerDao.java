/*
 * (C) Copyright Parasoft Corporation 2017.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.parabank.dao.jdbc;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

import com.parasoft.parabank.domain.Customer;

public class SecureJdbcCustomerDao extends JdbcCustomerDao {

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#updateCustomer(com.parasoft. parabank.domain.Customer)
     */
    @Override
    public void updateCustomer(final Customer customer) {
        final String SQL =
            "UPDATE Customer SET first_name = :firstName, last_name = :lastName, address = :address.street, city = :address.city, state = :address.state, zip_code = :address.zipCode, phone_number = :phoneNumber, ssn = :ssn, username = :username, password = :password WHERE id = :id";

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(customer);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(customer));
        log.info("Updated information for customer with id = " + customer.getId());
    }
}
