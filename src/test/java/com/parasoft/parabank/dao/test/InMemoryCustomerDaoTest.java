package com.parasoft.parabank.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.parasoft.parabank.dao.CustomerDao;
import com.parasoft.parabank.dao.InMemoryCustomerDao;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-25
 *
 */
public class InMemoryCustomerDaoTest extends AbstractParaBankTest {
    private static final int CUSTOMER1_ID = 1;

    private static final String CUSTOMER1_SSN = "123-45-6789";

    private static final String CUSTOMER1_USERNAME = "user1";

    private static final String CUSTOMER1_PASSWORD = "pass1";

    private static final int CUSTOMER2_ID = 2;

    private static final String CUSTOMER2_SSN = "987-65-4321";

    private static final String CUSTOMER2_USERNAME = "user2";

    private static final String CUSTOMER2_PASSWORD = "pass2";

    private CustomerDao customerDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final List<Customer> customers = new ArrayList<>();

        Customer customer = new Customer();
        customer.setId(CUSTOMER1_ID);
        customer.setSsn(CUSTOMER1_SSN);
        customer.setUsername(CUSTOMER1_USERNAME);
        customer.setPassword(CUSTOMER1_PASSWORD);
        customers.add(customer);

        customer = new Customer();
        customer.setId(CUSTOMER2_ID);
        customer.setSsn(CUSTOMER2_SSN);
        customer.setUsername(CUSTOMER2_USERNAME);
        customer.setPassword(CUSTOMER2_PASSWORD);
        customers.add(customer);

        customerDao = new InMemoryCustomerDao(customers);
    }

    @Test
    public void testCreateCustomer() {
        final Customer originalCustomer = new Customer();
        final int id = customerDao.createCustomer(originalCustomer);
        assertEquals(3, id);
        final Customer newCustomer = customerDao.getCustomer(id);
        assertEquals(originalCustomer, newCustomer);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = customerDao.getCustomer(CUSTOMER1_ID);
        assertNotNull(customer);
        assertEquals(CUSTOMER1_ID, customer.getId());
        customer = customerDao.getCustomer(CUSTOMER2_ID);
        assertNotNull(customer);
        assertEquals(CUSTOMER2_ID, customer.getId());
        assertNull(customerDao.getCustomer(-1));

        customer = customerDao.getCustomer(CUSTOMER1_SSN);
        assertNotNull(customer);
        assertEquals(CUSTOMER1_ID, customer.getId());
        customer = customerDao.getCustomer(CUSTOMER2_SSN);
        assertNotNull(customer);
        assertEquals(CUSTOMER2_ID, customer.getId());
        assertNull(customerDao.getCustomer("000-00-0000"));

        customer = customerDao.getCustomer(CUSTOMER1_USERNAME, CUSTOMER1_PASSWORD);
        assertNotNull(customer);
        assertEquals(CUSTOMER1_ID, customer.getId());
        customer = customerDao.getCustomer(CUSTOMER2_USERNAME, CUSTOMER2_PASSWORD);
        assertNotNull(customer);
        assertEquals(CUSTOMER2_ID, customer.getId());
        assertNull(customerDao.getCustomer("bad", "login"));
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = customerDao.getCustomer(CUSTOMER1_ID);
        customer.setFirstName("first");
        customer.setLastName("last");
        customerDao.updateCustomer(customer);
        customer = customerDao.getCustomer(CUSTOMER1_ID);
        assertEquals("first last", customer.getFullName());
    }
}
