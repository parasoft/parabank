package com.parasoft.parabank.dao.jdbc.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.parasoft.parabank.dao.internal.DynamicDataInserter;
import com.parasoft.parabank.dao.jdbc.JdbcSequenceDao;

public class StockDataInserter extends JdbcDaoSupport implements DynamicDataInserter {
    private static final Logger log = LoggerFactory.getLogger(StockDataInserter.class);

    private final SimpleDateFormat dateFormatter;

    private final Random random;

    private JdbcSequenceDao sequenceDao;

    public StockDataInserter() {
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        random = new Random(System.currentTimeMillis());
    }

    /** {@inheritDoc} */
    @Override
    public int getDataCount() {
        final int count = getJdbcTemplate().queryForObject("SELECT count(*) FROM Stock", Integer.class);
        return count;

    }

    private String getPrice() {
        final String priceDollars = String.valueOf(random.nextInt(90));
        String priceCents = String.valueOf(random.nextInt(100));
        priceCents = priceCents.length() == 1 ? "0" + priceCents : priceCents;
        return priceDollars + "." + priceCents;
    }

    public List<String> getSymbols() {
        final String SQL = "SELECT symbol FROM Company";

        return getJdbcTemplate().query(SQL, (ResultSetExtractor<List<String>>) rs -> {
            final List<String> symbols = new ArrayList<>();
            while (rs.next()) {
                symbols.add(rs.getString("symbol"));
            }
            return symbols;
        });
    }

    /** {@inheritDoc} */
    @Override
    public void insertData() {
        // TODO clean prior to inserting

        // final String SQL = "INSERT INTO Stock (id, symbol, date,
        // closing_price) " +
        //
        // // commented in order to make it database agnostic
        // // the below syntax is HSQL specific and so it is being replaced to a
        // // generic format
        // // "VALUES (:id, :symbol, :date, :closingPrice)";
        // "VALUES (?,?, ?, ?)";
        final String SQL_FMT = "INSERT INTO Stock (id, symbol, date, closing_price) VALUES (%1$d, \'%2$s\', \'%3$s\', \'%4$s\');";
        int nextId = sequenceDao.getNextId("Stock");
        log.debug("First Stock id = {}", nextId);

        nextId -= JdbcSequenceDao.OFFSET; // I need to this so there are no gaps in Ids
        int rows = 0;
        int totalRows = 0;
        int lastId = nextId;
        for (final String symbol : getSymbols()) {
            final Calendar calendar = Calendar.getInstance();
            final StringBuilder sb = new StringBuilder();
            final String price = getPrice();
            for (int i = -1; i > -1826; i--) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                // getJdbcTemplate().update(SQL, sequenceDao.getNextId("Stock"), symbol,
                // dateFormatter.format(calendar.getTime()), getPrice());
                nextId += JdbcSequenceDao.OFFSET;
                sb.append(String.format(SQL_FMT, nextId, symbol, dateFormatter.format(calendar.getTime()), price));
            }

            log.debug("processing stock {}", symbol);
            final String sql = sb.toString();
            rows = (nextId - lastId) / JdbcSequenceDao.OFFSET;
            totalRows += rows;
            getJdbcTemplate().update(sql);
            log.debug("Last {} Stock id = {}, inserted rows/total {}/{}", symbol, nextId, rows, nextId / JdbcSequenceDao.OFFSET);
            lastId = nextId;
            nextId = sequenceDao.setNextId("Stock", nextId); // this will catch up the Sequence
            nextId -= JdbcSequenceDao.OFFSET; // walk one back (we add it back just before insert)
            log.debug("done processing stock {}", symbol);
            if (log.isTraceEnabled()) {
                log.trace("First row: {}", sql.substring(0, sql.indexOf("INSERT", "INSERT".length())));
                log.trace("Last row : {}", sql.substring(sql.lastIndexOf("INSERT")));
            }
        }
        if (log.isTraceEnabled()) {
            log.trace("insertData added {} of {} rows", totalRows, getDataCount());
        }
    }

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }
}
