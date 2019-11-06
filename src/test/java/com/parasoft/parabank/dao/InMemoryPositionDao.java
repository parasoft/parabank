package com.parasoft.parabank.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parasoft.parabank.domain.HistoryPoint;
import com.parasoft.parabank.domain.Position;

public class InMemoryPositionDao implements PositionDao{
    private static int ID = 0;

    private final List<Position> positions;

    private final List<HistoryPoint> history;

    public InMemoryPositionDao(List<Position> positions, List<HistoryPoint> history) {
        this.positions = positions;
        ID = positions.size();

        this.history = history;
    }

    @Override
    public Position getPosition(int positionId) {
        for (Position position : positions) {
            if (position.getPositionId() == positionId) {
                return position;
            }
        }

        return null;
    }

    @Override
    public List<Position> getPositionsForCustomerId(int customerId) {
        List<Position> customerPositions = new ArrayList<>();

        for (Position position : positions) {
            if (position.getCustomerId() == customerId) {
                customerPositions.add(position);
            }
        }

        return customerPositions;
    }

    @Override
    public List<HistoryPoint> getPositionHistory(int positionId, Date startDate, Date endDate) {
        List<HistoryPoint> positionHistory = new ArrayList<>();

        for (Position position : positions) {
            for (HistoryPoint historyPoint : history) {
                if (historyPoint.getSymbol().equals(position.getSymbol())) {
                    positionHistory.add(historyPoint);
                }
            }
        }

        return positionHistory;
    }

    @Override
    public int createPosition(Position position) {
        position.setPositionId(++ID);
        positions.add(position);
        return ID;
    }

    @Override
    public boolean updatePosition(Position position) {
        boolean success = false;
        for (Position existingPosition : positions) {
            if (existingPosition.getPositionId() == position.getPositionId()) {
                positions.remove(existingPosition);
                positions.add(position);
                success = true;
                break;
            }
        }
        return success;
    }

    @Override
    public boolean deletePosition(Position position) {
        boolean success = false;
        for (Position existingPosition : positions) {
            if (existingPosition.getPositionId() == position.getPositionId()) {
                positions.remove(existingPosition);
                success = true;
                break;
            }
        }
        return success;
    }
}
