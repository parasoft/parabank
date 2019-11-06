package com.parasoft.parabank.dao;

import java.util.Date;
import java.util.List;

import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.Position;

/**
 * Interface for accessing Position information from a data source
 */
public interface PositionDao {

    /**
     * Retrieve a specific position by id
     *
     * @param positionId the position id to retrieve
     * @return position object representing the position
     */
    Position getPosition(int positionId);

    /**
     * Retrieve all positions for a given customer
     *
     * @param customerId the customer id to lookup
     * @return list of positions belonging to the given customer
     */
    List<Position> getPositionsForCustomerId(int customerId);

    /**
     * Return position history for a given position id
     * and date range
     *
     * @param positionId the position id
     * @param startDate the start date in the date range
     * @param endDate the end date in the date range
     * @return a list of history points
     */
    List<HistoryPoint> getPositionHistory(int positionId, Date startDate, Date endDate);

    /**
     * Add a new position to the data source
     *
     * Note that the position id will be automatically generated
     *
     * @param position the position information to store
     * @return generated position id
     */
    int createPosition(Position position);

    /**
     * Update stored information for a given position
     *
     * @param position the position to update
     * @return success
     */
    boolean updatePosition(Position position);

    /**
     * Delete a position from the data source
     *
     * @param position the position information to delete
     * @return success
     */
    boolean deletePosition(Position position);
}
