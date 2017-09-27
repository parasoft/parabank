package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.*;

import java.math.*;
import java.util.*;

import javax.annotation.*;

import org.junit.*;
import org.slf4j.*;
import org.springframework.dao.*;

import com.parasoft.parabank.dao.*;
import com.parasoft.parabank.dao.jdbc.internal.*;
import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.service.*;
import com.parasoft.parabank.test.util.*;

/**
 * @req PAR-16
 *
 */
public class JdbcPositionDaoTest extends AbstractParaBankDataSourceTest {
    private static final Logger log = LoggerFactory.getLogger(ParaBankServiceImplTest.class);

    private static final int CUSTOMER_ID = 12212;

    private static final String NAME = "Test Company";

    private static final String SYMBOL = "TC";

    private static final int SHARES = 20;

    private static final BigDecimal PURCHASEPRICE = new BigDecimal("50.0000");

    @Resource(name = "positionDao")
    private PositionDao positionDao;

    @Resource(name = "stockDataInserter")
    private StockDataInserter stockDataInserter;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        if (stockDataInserter.getDataCount() < 1) {
            stockDataInserter.insertData();
        }
    }

    public void setPositionDao(final PositionDao positionDao) {
        this.positionDao = positionDao;
    }

    public void setStockDataInserter(final StockDataInserter stockDataInserter) {
        this.stockDataInserter = stockDataInserter;
    }

    @Test
    public void testCreatePosition() {
        final Position setUpPosition = new Position();
        setUpPosition.setCustomerId(CUSTOMER_ID);
        setUpPosition.setName(NAME);
        setUpPosition.setSymbol(SYMBOL);
        setUpPosition.setShares(SHARES);
        setUpPosition.setPurchasePrice(PURCHASEPRICE);

        final int positionId = positionDao.createPosition(setUpPosition);
        assertEquals("wrong expected id?", 13017, positionId);

        final Position position = positionDao.getPosition(positionId);
        assertEquals(setUpPosition, position);
    }

    @Test
    public void testDeletePosition() {
        final Position setUpPosition = new Position();
        setUpPosition.setCustomerId(CUSTOMER_ID);
        setUpPosition.setName(NAME);
        setUpPosition.setSymbol(SYMBOL);
        setUpPosition.setShares(SHARES);
        setUpPosition.setPurchasePrice(PURCHASEPRICE);

        final int positionId = positionDao.createPosition(setUpPosition);

        Position position = positionDao.getPosition(positionId);
        assertEquals(setUpPosition, position);

        positionDao.deletePosition(position);
        try {
            position = positionDao.getPosition(positionId);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetPosition() {
        Position position = positionDao.getPosition(12345);
        assertEquals(12345, position.getPositionId());
        assertEquals(12212, position.getCustomerId());
        assertEquals("AMR Corporation", position.getName());
        assertEquals("AAR", position.getSymbol());
        assertEquals(20, position.getShares());
        // changed comparison to floats to remove the precision 1351.12 v.s.
        // 1351.1200 issue
        assertEquals(new BigDecimal("23.53").floatValue(), position.getPurchasePrice().floatValue(), 0.0001f);

        try {
            position = positionDao.getPosition(-1);
            fail("did not throw expected DataAccessException");
        } catch (final DataAccessException e) {
        }
    }

    @Test
    public void testGetPositionHistory() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -10);
        final Date startDate = calendar.getTime();
        final List<HistoryPoint> history = positionDao.getPositionHistory(12345, startDate, endDate);
        int expected = 11;
        if (history.size() == 22) { // TODO figure out why when this test class
                                    // is run by in isolation the result is 11
                                    // if run as part all tests it's 22
            expected = 22;
        }
        log.debug("using expected {} value", expected);
        assertEquals("wrong number of history points?", expected, history.size());

        for (final HistoryPoint historyPoint : history) {
            assertEquals("wrong symbol?", "AAR", historyPoint.getSymbol());
        }
    }

    @Test
    public void testGetPositionsForCustomerId() {
        List<Position> positions = positionDao.getPositionsForCustomerId(12212);
        assertEquals("wrong number of positions?", 3, positions.size());

        positions = positionDao.getPositionsForCustomerId(-1);
        assertEquals("expected no positions for invalid id", 0, positions.size());
    }

    @Test
    public void testUpdatePosition() {
        final Position setUpPosition = new Position();
        setUpPosition.setCustomerId(CUSTOMER_ID);
        setUpPosition.setName(NAME);
        setUpPosition.setSymbol(SYMBOL);
        setUpPosition.setShares(SHARES);
        setUpPosition.setPurchasePrice(PURCHASEPRICE);

        final int positionId = positionDao.createPosition(setUpPosition);

        final Position position = positionDao.getPosition(positionId);
        assertEquals(setUpPosition, position);

        position.setShares(10);
        assertFalse(setUpPosition.equals(position));

        positionDao.updatePosition(position);

        final Position updatedPosition = positionDao.getPosition(positionId);
        assertEquals(position, updatedPosition);
    }
}
