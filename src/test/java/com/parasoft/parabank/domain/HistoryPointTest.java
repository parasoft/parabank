package com.parasoft.parabank.domain;

import static org.junit.Assert.*;

import java.math.*;
import java.util.*;

import org.junit.*;

import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-28
 *
 */
public class HistoryPointTest extends AbstractBeanTestCase<HistoryPoint> {
    private static final String SYMBOL = "AAR";

    private static final BigDecimal CLOSING_PRICE = new BigDecimal("30.00");

    @Test
    public void testGetAndSetClosingPrice() {
        assertNull(bean.getClosingPrice());
        bean.setClosingPrice(CLOSING_PRICE);
        assertEquals(CLOSING_PRICE.floatValue(), bean.getClosingPrice().floatValue(), 0.0001f);
    }

    @Test
    public void testGetAndSetDate() {
        assertNull(bean.getDate());
        final Date date = Calendar.getInstance().getTime();
        bean.setDate(date);
        assertEquals(date, bean.getDate());
    }

    @Test
    public void testGetAndSetSymbol() {
        assertNull(bean.getSymbol());
        bean.setSymbol(SYMBOL);
        assertEquals(SYMBOL, bean.getSymbol());
    }
}
