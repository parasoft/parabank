package com.parasoft.parabank.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.parasoft.parabank.dao.PositionDao;
import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.Position;

/*
 * JDBC implementation of PositionDao
 */
public class JdbcPositionDao extends NamedParameterJdbcDaoSupport implements PositionDao {
    private static class HistoryPointMapper implements RowMapper<HistoryPoint> {
        @Override
        public HistoryPoint mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final HistoryPoint historyPoint = new HistoryPoint();
            historyPoint.setSymbol(rs.getString("symbol"));
            historyPoint.setDate(rs.getDate("date"));
            historyPoint.setClosingPrice(rs.getBigDecimal("closing_price"));
            return historyPoint;
        }
    }

    private static class PositionMapper implements RowMapper<Position> {
        @Override
        public Position mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final Position position = new Position();
            position.setPositionId(rs.getInt("position_id"));
            position.setCustomerId(rs.getInt("customer_id"));
            position.setName(rs.getString("name"));
            position.setSymbol(rs.getString("symbol"));
            position.setShares(rs.getInt("shares"));
            position.setPurchasePrice(rs.getBigDecimal("purchase_price"));
            return position;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(JdbcPositionDao.class);

    private final String BASE_QUERY_SQL =
        "SELECT position_id, customer_id, name, symbol, shares, purchase_price FROM Positions";

    private JdbcSequenceDao sequenceDao;

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#createPosition(com.parasoft. parabank.domain.Position)
     */
    @Override
    public int createPosition(final Position position) {
        final String SQL =
            "INSERT INTO Positions (position_id, customer_id, name, symbol, shares, purchase_price) VALUES (:positionId, :customerId, :name, :symbol, :shares, :purchasePrice)";

        final int position_id = sequenceDao.getNextId("Position");
        position.setPositionId(position_id);
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(position);
        getNamedParameterJdbcTemplate().update(SQL, source);

        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(position));
        log.info("Created new position with position id = " + position_id);

        return position_id;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#deletePosition(com.parasoft. parabank.domain.Position)
     */
    @Override
    public boolean deletePosition(final Position position) {
        final boolean success = false;
        final String SQL = "DELETE FROM Positions WHERE position_id = :positionId";

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(position);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(position));
        log.info("Deleted position with position id = " + position.getPositionId());
        return success; // parasoft-suppress PB.USC.CC "The logic in the method will be modified in the next sprint. Reviewed and found appropriate."
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#getPosition(int)
     */
    @Override
    public Position getPosition(final int position_id) {
        final String SQL = BASE_QUERY_SQL + " WHERE position_id = ?";

        log.info("Getting position object for position id = " + position_id);
        final Position position = getJdbcTemplate().queryForObject(SQL, new PositionMapper(), position_id);

        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#getPositionHistory(int, java.util.Date, java.util.Date)
     */
    @Override
    public List<HistoryPoint> getPositionHistory(final int positionId, final Date startDate, final Date endDate) {
        final String SQL = "SELECT * FROM Stock WHERE symbol = :symbol " + "AND date BETWEEN :endDate AND :startDate";

        final String symbol = getPosition(positionId).getSymbol();

        final List<HistoryPoint> history =
            getJdbcTemplate().query(SQL, new HistoryPointMapper(), symbol, startDate, endDate);

        log.info("Retrieved position history for position #" + positionId + " and date range " + startDate + " to "
            + endDate);

        return history;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#getPositionsForCustomerId(int)
     */
    @Override
    public List<Position> getPositionsForCustomerId(final int customer_id) {
        final String SQL = BASE_QUERY_SQL + " WHERE customer_id = ?";

        final List<Position> positions = getJdbcTemplate().query(SQL, new PositionMapper(), customer_id);
        log.info("Retrieved " + positions.size() + " positions for customer id = " + customer_id);

        return positions;
    }

    public void setSequenceDao(final JdbcSequenceDao sequenceDao) {
        this.sequenceDao = sequenceDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.PositionDao#updatePosition(com.parasoft. parabank.domain.Position)
     */
    @Override
    public boolean updatePosition(final Position position) {
        final boolean success = false;
        final String SQL = "UPDATE Positions SET shares = :shares WHERE position_id = :positionId";

        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(position);
        getNamedParameterJdbcTemplate().update(SQL, source);
        // getJdbcTemplate().update(SQL, new
        // BeanPropertySqlParameterSource(position));
        log.info("Updated shares information for position with position id = " + position.getPositionId());
        return success;
    }
}
