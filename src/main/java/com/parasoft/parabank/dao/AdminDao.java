package com.parasoft.parabank.dao;

import java.util.Map;

/**
 * Utility methods for maintaining the items in the data source
 */
public interface AdminDao {

    /**
     * Initialize the data source with the full set of sample data
     *
     * Run this first after installing ParaBank
     * @return
     */
      void initializeDB();

    /**
     * Reset the data source and populate it with a subset of sample data
     *
     * Run this before performing a demo
     */
    void cleanDB();

    /**
     * Gets the value of a given configuration parameter
     *
     * @param name the name of the parameter
     * @return the value of the parameter
     */
    String getParameter(String name);

    /**
     * Sets the value of a given configuration parameter
     *
     * @param name the name of the parameter
     * @param value the value to set
     */
    void setParameter(String name, String value);

    /**
     * Gets all stored parameters
     *
     * @return all stored parameters as a map
     */
    Map<String, String> getParameters();
}
