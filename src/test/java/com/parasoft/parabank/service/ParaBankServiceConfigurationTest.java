package com.parasoft.parabank.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-43
 *
 */
public class ParaBankServiceConfigurationTest extends AbstractParaBankTest {
    private ParaBankServiceConfiguration configuration;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        configuration = new ParaBankServiceConfiguration();
    }

    @Test
    public void testGetWrapperPartMinOccurs() {
        assertEquals(new Long(1), configuration.getWrapperPartMinOccurs(null));
    }

    @Test
    public void testIsWrapperPartNillable() {
        assertEquals(Boolean.FALSE, configuration.isWrapperPartNillable(null));
    }
}
