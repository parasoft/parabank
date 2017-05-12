package com.parasoft.parabank.domain;

import static org.junit.Assert.*;

import org.junit.*;

import com.parasoft.parabank.test.util.*;

public class CustomerTest extends AbstractBeanTestCase<Customer> {
    @Test
    public void testGetFullName() {
        bean.setFirstName("first");
        bean.setLastName("last");
        assertEquals("first last", bean.getFullName());
    }
}
