package com.parasoft.parabank.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.parasoft.parabank.test.util.AbstractBeanTestCase;

public class PayeeTest extends AbstractBeanTestCase<Payee> {

    @Test
    @Override
    public void testEqualsContractMet() {
        // Do not use the normal test for equals(), since ContactInformation has a known (and desired) bug in equals()
        Object o1;
        Object o2;
        try {
            o1 = beanClass.newInstance();
            o2 = beanClass.newInstance();

            assertTrue("Instances with default constructor not equal (o1.equals(o2))", o1.equals(o2));
            assertTrue("Instances with default constructor not equal (o2.equals(o1))", o2.equals(o1));
        } catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new AssertionError("Unable to construct an instance of the class under test:" + beanClass.getName());
        }
    }
}
