package com.parasoft.parabank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.parasoft.parabank.test.util.AbstractBeanTestCase;

public class CustomerTest extends AbstractBeanTestCase<Customer> {
    @Test
    public void testGetFullName() {
        bean.setFirstName("first");
        bean.setLastName("last");
        assertEquals("first last", bean.getFullName());
    }
}
