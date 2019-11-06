package com.parasoft.parabank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.parasoft.parabank.test.util.AbstractBeanTestCase;

/**
 * @req PAR-30
 *
 */
public class PositionTest extends AbstractBeanTestCase<Position> {
    private static final int POSITION1_ID = 1;

    private static final int POSITION2_ID = 2;

    private static final int CUSTOMER1_ID = 1;

    private static final int CUSTOMER2_ID = 2;

    private static final String NAME = "Test Company";

    private static final String SYMBOL = "TC";

    private static final int SHARES1 = 1;

    private static final int SHARES2 = 2;

    private static final BigDecimal PURCHASE_PRICE1 = new BigDecimal(100.00);

    private static final BigDecimal PURCHASE_PRICE2 = new BigDecimal(50.00);

    @Test
    public void testGetAndSetPositionId() {
        bean.setPositionId(POSITION1_ID);
        assertEquals(POSITION1_ID, bean.getPositionId());
        bean.setPositionId(POSITION2_ID);
        assertEquals(POSITION2_ID, bean.getPositionId());
    }

    @Test
    public void testGetAndSetCustomerId() {
        bean.setCustomerId(CUSTOMER1_ID);
        assertEquals(CUSTOMER1_ID, bean.getCustomerId());
        bean.setCustomerId(CUSTOMER2_ID);
        assertEquals(CUSTOMER2_ID, bean.getCustomerId());
    }

    @Test
    public void testGetAndSetName() {
        assertNull(bean.getName());
        bean.setName(NAME);
        assertEquals(NAME, bean.getName());
    }

    @Test
    public void testGetAndSetSymbol() {
        assertNull(bean.getSymbol());
        bean.setSymbol(SYMBOL);
        assertEquals(SYMBOL, bean.getSymbol());
    }

    @Test
    public void testGetAndSetShares() {
        bean.setShares(SHARES1);
        assertEquals(SHARES1, bean.getShares());
        bean.setShares(SHARES2);
        assertEquals(SHARES2, bean.getShares());
    }

    @Test
    public void testGetAndSetPurchasePrice() {
        bean.setPurchasePrice(PURCHASE_PRICE1);
        assertEquals(PURCHASE_PRICE1, bean.getPurchasePrice());
        bean.setPurchasePrice(PURCHASE_PRICE2);
        assertEquals(PURCHASE_PRICE2, bean.getPurchasePrice());
    }

    @Test
    public void testGetCostBasis() {
        bean.setPurchasePrice(PURCHASE_PRICE1);
        assertEquals(PURCHASE_PRICE1, bean.getPurchasePrice());
        bean.setShares(SHARES1);
        assertEquals(SHARES1, bean.getShares());
        assertEquals(new BigDecimal(100), bean.getCostBasis());
    }
}
