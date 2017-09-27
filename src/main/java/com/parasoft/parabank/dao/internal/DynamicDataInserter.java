package com.parasoft.parabank.dao.internal;

/**
 * Interface for inserting dynamic data into the database at creation time
 */
public interface DynamicDataInserter {

    /**
     * Called to see if the data inserter is being called twice
     */
    int getDataCount();

    /**
     * Called at DB creation time to insert dynamic data into the DB
     */
    void insertData();
}
