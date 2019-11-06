package com.parasoft.bookstore;

import java.sql.SQLException;

/**
 * BookStoreMemoryDB
 */
public class BookStoreMemoryDB extends DB {
    private static BookStoreMemoryDB db = null;

    /**
     * @throws SQLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public BookStoreMemoryDB()
        throws SQLException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        super();
        // RJ Auto-generated constructor stub
    }

    public static BookStoreMemoryDB getDBInstance()
        throws SQLException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        if (db == null) {
            db = new BookStoreMemoryDB();
        }
        return db;
    }
}