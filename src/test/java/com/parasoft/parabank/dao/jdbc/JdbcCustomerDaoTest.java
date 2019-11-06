package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;

import com.parasoft.parabank.dao.CustomerDao;
import com.parasoft.parabank.domain.Address;
import com.parasoft.parabank.domain.Customer;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;
/**
 * @req PAR-13
 *
 */
public class JdbcCustomerDaoTest extends AbstractParaBankDataSourceTest {
    private static final String FIRST_NAME = "Steve";

    private static final String LAST_NAME = "Jobs";

    private static final String STREET = "1 Infinite Loop";

    private static final String CITY = "Cupertino";

    private static final String STATE = "CA";

    private static final String ZIP_CODE = "95014";

    private static final String PHONE_NUMBER = "1-800-MY-APPLE";

    private static final String SSN = "666-66-6666";

    private static final String USERNAME = "steve";

    private static final String PASSWORD = "jobs";

    @Resource(name = "customerDao")
    private CustomerDao customerDao;

    private Customer customer;

    private void defaultCustomerTest(final Customer customer) {
        assertEquals(12212, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("1431 Main St", customer.getAddress().getStreet());
        assertEquals("Beverly Hills", customer.getAddress().getCity());
        assertEquals("CA", customer.getAddress().getState());
        assertEquals("90210", customer.getAddress().getZipCode());
        assertEquals("310-447-4121", customer.getPhoneNumber());
        assertEquals("622-11-9999", customer.getSsn());
        assertEquals("john", customer.getUsername());
        assertEquals("demo", customer.getPassword());
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        customer = new Customer();
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        final Address address = new Address();
        address.setStreet(STREET);
        address.setCity(CITY);
        address.setState(STATE);
        address.setZipCode(ZIP_CODE);
        customer.setAddress(address);
        customer.setPhoneNumber(PHONE_NUMBER);
        customer.setSsn(SSN);
        customer.setUsername(USERNAME);
        customer.setPassword(PASSWORD);
    }

    public void setCustomerDao(final CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Test
    public void testCreateCustomer() {
        final int id = customerDao.createCustomer(customer);
        assertEquals("wrong expected id?", 12434, id);

        final Customer customer = customerDao.getCustomer(id);
        assertFalse(this.customer == customer);
        assertEquals(this.customer, customer);
    }

    @Test
    public void testGetCustomer() {
        Customer customer = customerDao.getCustomer(12212);
        defaultCustomerTest(customer);

        try {
            customer = customerDao.getCustomer(-1);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetCustomerBySSN() {
        final Customer customer = customerDao.getCustomer("622-11-9999");
        defaultCustomerTest(customer);

        assertNull(customerDao.getCustomer(null));
        assertNull(customerDao.getCustomer("foo"));
        assertNull(customerDao.getCustomer("111-11-1111"));
    }

    @Test
    public void testGetCustomerByUsername() {
        final Customer customer = customerDao.getCustomer("john", "demo");
        defaultCustomerTest(customer);

        assertNull(customerDao.getCustomer(null, null));
        assertNull(customerDao.getCustomer("john", null));
        assertNull(customerDao.getCustomer(null, "demo"));
        assertNull(customerDao.getCustomer("john", "foo"));
        assertNull(customerDao.getCustomer("foo", "demo"));
        assertNull(customerDao.getCustomer("foo", "bar"));
    }

    @Test
    public void testUpdateCustomer() {
        final int id = customerDao.createCustomer(customer);

        final Customer customer = customerDao.getCustomer(id);
        assertFalse(this.customer == customer);
        assertEquals(this.customer, customer);

        customer.setFirstName(customer.getFirstName() + "*");
        customer.setLastName(customer.getLastName() + "*");
        final Address address = new Address();
        address.setStreet(customer.getAddress().getStreet() + "*");
        address.setCity(customer.getAddress().getCity() + "*");
        address.setState(customer.getAddress().getState() + "*");
        address.setZipCode(customer.getAddress().getZipCode() + "*");
        customer.setAddress(address);
        customer.setPhoneNumber(customer.getPhoneNumber() + "*");
        customer.setSsn(customer.getSsn() + "*");
        customer.setUsername(customer.getUsername() + "*");
        customer.setPassword(customer.getPassword() + "*");

        customerDao.updateCustomer(customer);

        final Customer updatedCustomer = customerDao.getCustomer(id);
        assertFalse(customer == updatedCustomer);
        assertFalse(this.customer.equals(updatedCustomer));
        assertEquals(customer, updatedCustomer);
    }
}
