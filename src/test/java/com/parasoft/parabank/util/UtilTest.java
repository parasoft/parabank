package com.parasoft.parabank.util;

import static org.junit.Assert.*;

import org.junit.*;

import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-44
 *
 */
public class UtilTest extends AbstractParaBankTest {
    @Test
    public void testEmpty() {
        assertTrue(Util.isEmpty(null));
        assertTrue(Util.isEmpty(""));
        assertFalse(Util.isEmpty("s"));
    }

    @Test
    public void testEquals() {
        assertTrue(Util.equals(null, null));
        assertFalse(Util.equals("foo", null));
        assertFalse(Util.equals(null, "bar"));
        assertFalse(Util.equals("foo", "bar"));
        assertTrue(Util.equals("foo", "foo"));
    }
}
