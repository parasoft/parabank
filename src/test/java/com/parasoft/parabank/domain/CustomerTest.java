package com.parasoft.parabank.domain;

import static org.junit.Assert.*;

import org.junit.*;

import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-26
 *
 */
public class CustomerTest extends AbstractBeanTestCase<Customer> {
    @Test
    public void testGetFullName() {
        bean.setFirstName("first");
        bean.setLastName("last");
        assertEquals("first last", bean.getFullName());
    }
}
