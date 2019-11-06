/*
 * (C) Copyright Parasoft Corporation 2017.  All rights reserved.
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Parasoft
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package com.parasoft.bookstore;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecureCartService extends CartService {
    /**
     *
     * @see com.parasoft.parabank.store.ICart#getItemByTitle(java.lang.String)
     */
    @Override
    public Book[] getItemByTitle(String title) throws Exception {
        ++invocationCounter;
        Book[] books = getByTitleLike(title != null? title : "");
        for (Book b : books) {
            b.inflatePrice(new BigDecimal(invocationCounter/5));
        }
        return books;
    }

    /**
     * @param titlePart a keyword in the title of the book
     * @return Vector <Book>
     */
    private static Book[] getByTitleLike(String titlePart) throws SQLException, InstantiationException,
    IllegalAccessException, ClassNotFoundException, ItemNotFoundException {
        String query = "SELECT DISTINCT " +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_ID + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_ISBN + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_TITLE + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_YEAR + "," +
                BookStoreDB.NL_TABLE_PUBLISHER + "." + BookStoreDB.NL_NAME + " as " + BookStoreDB.NL_PUBLISHER_NAME + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_DESCRIPTION + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_PRICE + "," +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_STOCK +
                " FROM " +
                BookStoreDB.NL_TABLE_BOOK + "," +
                BookStoreDB.NL_TABLE_AUTHOR + "," +
                BookStoreDB.NL_TABLE_PUBLISHER +
                " WHERE " +
                "LCASE(" + BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_TITLE + ")" + " LIKE ? AND " +
                BookStoreDB.NL_TABLE_BOOK + "." + BookStoreDB.NL_ISBN + " = " +
                BookStoreDB.NL_TABLE_AUTHOR + "." + BookStoreDB.NL_ISBN + " AND " +
                BookStoreDB.NL_TABLE_BOOK + ".publisher_id = " +
                BookStoreDB.NL_TABLE_PUBLISHER + "." + BookStoreDB.NL_ID;
        BookStoreDB db = BookStoreDB.getDBInstance();
        PreparedStatement stmt = db.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        stmt.setString(1, "%" + titlePart.toLowerCase() + "%");
        Book[] books= BookStoreDB.getByTitleLike(stmt.executeQuery(), titlePart);
        stmt.close();
        return books;
    }
}
