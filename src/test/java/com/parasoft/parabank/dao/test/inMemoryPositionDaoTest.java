package com.parasoft.parabank.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.parasoft.parabank.dao.InMemoryPositionDao;
import com.parasoft.parabank.dao.PositionDao;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.Position;
import com.parasoft.parabank.test.util.AbstractParaBankTest;

/**
 * @req PAR-25
 *
 */
public class inMemoryPositionDaoTest extends AbstractParaBankTest {
    private static final int POSITION1_ID = 1;

    private static final int POSITION2_ID = 2;

    private static final int CUSTOMER_ID = 101;

    private static final String SYMBOL = "AAR";

    private static final BigDecimal CLOSING_PRICE1 = new BigDecimal("30.00");

    private static final BigDecimal CLOSING_PRICE2 = new BigDecimal("33.00");

    private PositionDao positionDao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        final List<Position> positions = new ArrayList<>();

        Position position = new Position();
        position.setPositionId(POSITION1_ID);
        position.setCustomerId(CUSTOMER_ID);
        position.setSymbol(SYMBOL);
        positions.add(position);

        position = new Position();
        position.setPositionId(POSITION2_ID);
        position.setCustomerId(CUSTOMER_ID);
        position.setSymbol(SYMBOL);
        positions.add(position);

        final List<HistoryPoint> history = new ArrayList<>();

        HistoryPoint historyPoint = new HistoryPoint();
        historyPoint.setSymbol(SYMBOL);
        historyPoint.setClosingPrice(CLOSING_PRICE1);
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        historyPoint.setDate(calendar.getTime());
        history.add(historyPoint);

        historyPoint = new HistoryPoint();
        historyPoint.setSymbol(SYMBOL);
        historyPoint.setClosingPrice(CLOSING_PRICE2);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        historyPoint.setDate(calendar.getTime());
        history.add(historyPoint);

        positionDao = new InMemoryPositionDao(positions, history);
    }

    @Test
    public void testCreatePosition() {
        final Position originalPosition = new Position();
        final int positionId = positionDao.createPosition(originalPosition);
        assertEquals(3, positionId);
        final Position newPosition = positionDao.getPosition(positionId);
        assertEquals(originalPosition, newPosition);
    }

    @Test
    public void testDeletePosition() {
        Position position = new Position();
        final int positionId = positionDao.createPosition(position);
        assertEquals(3, positionId);
        position = positionDao.getPosition(positionId);
        positionDao.deletePosition(position);
        assertNull(positionDao.getPosition(positionId));
    }

    @Test
    public void testGetPosition() {
        Position position = positionDao.getPosition(POSITION1_ID);
        assertNotNull(position);
        assertEquals(POSITION1_ID, position.getPositionId());

        position = positionDao.getPosition(POSITION2_ID);
        assertNotNull(position);
        assertEquals(POSITION2_ID, position.getPositionId());

        assertNull(positionDao.getPosition(-1));
    }

    @Test
    public void testGetPositionHistory() {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final Date endDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        final Date startDate = calendar.getTime();
        List<HistoryPoint> history = positionDao.getPositionHistory(POSITION1_ID, startDate, endDate);

        assertEquals("wrong number of history points?", 4, history.size());

        for (final HistoryPoint historyPoint : history) {
            assertEquals("wrong symbol?", "AAR", historyPoint.getSymbol());
        }

        history = positionDao.getPositionHistory(POSITION2_ID, startDate, endDate);

        assertEquals("wrong number of history points?", 4, history.size());

        for (final HistoryPoint historyPoint : history) {
            assertEquals("wrong symbol?", "AAR", historyPoint.getSymbol());
        }
    }

    @Test
    public void testGetPositionsForCustomerId() {
        final List<Position> positions = positionDao.getPositionsForCustomerId(CUSTOMER_ID);

        assertNotNull(positions);
        assertEquals("expected 2 positions for customer id", 2, positions.size());
    }

    @Test
    public void testUpdatePosition() {
        Position position = positionDao.getPosition(POSITION1_ID);
        position.setShares(10);
        positionDao.updatePosition(position);
        position = positionDao.getPosition(POSITION1_ID);
        assertEquals(10, position.getShares());
    }
}
