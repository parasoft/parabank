package com.parasoft.parabank.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.parasoft.parabank.dao.CustomerDao;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Customer;

/*
 * JDBC implementation of CustomerDao
 */
public class JdbcCustomerDao extends NamedParameterJdbcDaoSupport implements CustomerDao {
    private static class CustomerMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            final Address address = new Address();
            address.setStreet(rs.getString("address"));
            address.setCity(rs.getString("city"));
            address.setState(rs.getString("state"));
            address.setZipCode(rs.getString("zip_code"));
            customer.setAddress(address);
            customer.setPhoneNumber(rs.getString("phone_number"));
            customer.setSsn(rs.getString("ssn"));
            customer.setUsername(rs.getString("username"));
            customer.setPassword(rs.getString("password"));
            return customer;
        }
    }

    protected static final Logger log = LoggerFactory.getLogger(JdbcCustomerDao.class);

    private final String BASE_QUERY_SQL =
        "SELECT id, first_name, last_name, address, city, state, zip_code, phone_number, ssn, username, password FROM Customer";

    private JdbcSequenceDao sequenceDao;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#createCustomer(com.parasoft. parabank.domain.Customer)
     */
    @Override
    public int createCustomer(final Customer customer) {
        final String SQL =
            "INSERT INTO Customer (id, first_name, last_name, address, city, state, zip_code, phone_number, ssn, username, password) VALUES (:id, :firstName, :lastName, :address.street, :address.city, :address.state, :address.zipCode, :phoneNumber, :ssn, :username, :password)";

        final int id = sequenceDao.getNextId("Customer");
        customer.setId(id);
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(customer);
        getNamedParameterJdbcTemplate().update(SQL, source);

        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(customer));
        log.info("Created new customer with id = " + id);

        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#getCustomer(int)
     */
    @Override
    public Customer getCustomer(final int id) {
        final String SQL = BASE_QUERY_SQL + " WHERE id = ?";

        log.info("Getting customer object for id = " + id);
        final Customer customer = getJdbcTemplate().queryForObject(SQL, new CustomerMapper(), id);

        return customer;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#getCustomer(java.lang.String)
     */
    @Override
    public Customer getCustomer(final String ssn) {
        final String SQL = BASE_QUERY_SQL + " WHERE ssn = ?";

        Customer customer = null;

        try {
            log.info("Getting customer object for ssn = " + ssn);
            customer = getJdbcTemplate().queryForObject(SQL, new CustomerMapper(), ssn);
        } catch (final DataAccessException e) {
            log.warn("Invalid customer lookup attempt with ssn = " + ssn);
        }

        return customer;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#getCustomer(java.lang.String, java.lang.String)
     */
    @Override
    public Customer getCustomer(final String username, final String password) {
        final String SQL = BASE_QUERY_SQL + " WHERE username = ? and password = ?";

        Customer customer = null;

        try {
            log.info("Getting customer object for username = " + username + " and password = " + password);
            customer = getJdbcTemplate().queryForObject(SQL, new CustomerMapper(), username, password);
        } catch (final DataAccessException e) {
            log.warn("Invalid login attempt with username = " + username + " and password = " + password);
        }

        return customer;
    }

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.CustomerDao#updateCustomer(com.parasoft. parabank.domain.Customer)
     */
    @Override
    public void updateCustomer(final Customer customer) {
        // Purposely introduce a security failure to demonstrate SQL injections in the REST API
        final String SQL =
                "UPDATE Customer SET first_name = '" + customer.getFirstName() + "', last_name = :lastName, address = :address.street, city = :address.city, state = :address.state, zip_code = :address.zipCode, phone_number = :phoneNumber, ssn = :ssn, username = :username, password = :password WHERE id = :id";

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(customer);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(customer));
        log.info("Updated information for customer with id = " + customer.getId());
    }
}
