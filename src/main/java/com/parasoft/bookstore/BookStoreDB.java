package com.parasoft.bookstore;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class BookStoreDB extends DB {
    private static final int MAX_BOOKS_TO_ADD = 1000;

    static final String NL_TABLE_BOOK      = "book";
    static final String NL_TABLE_AUTHOR    = "author";
    static final String NL_TABLE_PUBLISHER = "publisher";
    // column names
    static final String NL_ID          = "id";
    static final String NL_ISBN        = "isbn";
    static final String NL_TITLE       = "title";
    static final String NL_YEAR        = "year";
    static final String NL_NAME        = "name";
    static final String NL_DESCRIPTION = "description";
    static final String NL_PRICE       = "price";
    static final String NL_STOCK       = "stock";
    // aliases
    static final String NL_PUBLISHER_NAME = "PN";
    private static final String NL_AUTHOR_NAME = "AN";

    private static BookStoreDB db = null;
    // virtual database for books added by clients
    private static Hashtable<Integer, TempBook> addedBooks;

    private BookStoreDB()
        throws SQLException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        super();
    }

    public static BookStoreDB getDBInstance()
        throws SQLException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException
    {
        if (db == null) {
            db = new BookStoreDB();
        } else if (db.isClosed()) {
            db.connect();
        }
        return db;
    }

    public static Book[] getByTitleLike(String titlePart) throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, ItemNotFoundException {
        String query = "SELECT DISTINCT " +
                NL_TABLE_BOOK + "." + NL_ID + "," +
                NL_TABLE_BOOK + "." + NL_ISBN + "," +
                NL_TABLE_BOOK + "." + NL_TITLE + "," +
                NL_TABLE_BOOK + "." + NL_YEAR + "," +
                NL_TABLE_PUBLISHER + "." + NL_NAME + " as " + NL_PUBLISHER_NAME + "," +
                NL_TABLE_BOOK + "." + NL_DESCRIPTION + "," +
                NL_TABLE_BOOK + "." + NL_PRICE + "," +
                NL_TABLE_BOOK + "." + NL_STOCK +
                " FROM " +
                NL_TABLE_BOOK + "," +
                NL_TABLE_AUTHOR + "," +
                NL_TABLE_PUBLISHER +
                " WHERE " +
                "LCASE(" + NL_TABLE_BOOK + "." + NL_TITLE + ")" + " LIKE '%"+ titlePart.toLowerCase() +"%' AND " +
                NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                NL_TABLE_AUTHOR + "." + NL_ISBN + " AND " +
                NL_TABLE_BOOK + ".publisher_id = " +
                NL_TABLE_PUBLISHER + "." + NL_ID;
        BookStoreDB db = getDBInstance();
        Statement stmt = db.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        Book[] books= getByTitleLike(stmt.executeQuery(query), titlePart);
        stmt.close();
        return books;
    }

    static Book[] getByTitleLike(ResultSet rs, String titlePart) throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, ItemNotFoundException {

        boolean hasNext = rs.first();
        Vector<Book> books = new Vector<>();

        String query2 = "SELECT " +
                NL_TABLE_AUTHOR + "." + NL_NAME + " as " + NL_AUTHOR_NAME +
                " FROM " +
                NL_TABLE_BOOK + "," +
                NL_TABLE_AUTHOR + "," +
                NL_TABLE_PUBLISHER +
                " WHERE " +
                "LCASE(" + NL_TABLE_BOOK + "." + NL_TITLE + ")" + " LIKE ? AND " +
                NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                NL_TABLE_AUTHOR + "." + NL_ISBN + " AND " +
                NL_TABLE_BOOK + ".publisher_id = " +
                NL_TABLE_PUBLISHER + "." + NL_ID + " AND " +
                NL_TABLE_AUTHOR + "." + NL_ISBN + " = ?";
        while (hasNext) {
            int id = rs.getInt(NL_ID);
            String isbn = rs.getString(NL_ISBN);
            String title = rs.getString(NL_TITLE);
            Date year = rs.getDate(NL_YEAR);
            String publisher = rs.getString(NL_PUBLISHER_NAME);
            String description = rs.getString(NL_DESCRIPTION);
            BigDecimal price = rs.getBigDecimal(NL_PRICE);
            int stock = rs.getInt(NL_STOCK);

            PreparedStatement stmt2 = db.prepareStatement(query2,
                                                          ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                          ResultSet.CONCUR_UPDATABLE );
            stmt2.setString(1, "%" + titlePart.toLowerCase() + "%");
            stmt2.setString(2, isbn);
            ResultSet rs2 = stmt2.executeQuery();
            boolean hasMore = rs2.first();
            Vector<String> authors = new Vector<>();

            while (hasMore) {
                String author = rs2.getString(NL_AUTHOR_NAME);
                authors.add(author);
                hasMore = rs2.next();
            }

            String arrayOfAuthors[] = new String[authors.size()];

            for (int i = 0; i < arrayOfAuthors.length; ++i) {
                arrayOfAuthors[i] = authors.elementAt(i);
            }

            Book book = new Book(id, isbn, title, year, arrayOfAuthors, publisher,
                                 description, price, stock);
            books.add(book);
            hasNext = rs.next();
        }

        if (addedBooks != null) {
            Enumeration<TempBook> enum_var = addedBooks.elements();
            while (enum_var.hasMoreElements()) {
                TempBook b = enum_var.nextElement();
                if (b.getBook().getName() != null && b.getBook().getName().indexOf(titlePart) != -1) {
                    books.add(b.getBook());
                }
            }
        }

        Book arrayOfBooks[] = new Book[books.size()];

        for (int i = 0; i < arrayOfBooks.length; ++i) {
            arrayOfBooks[i] = books.elementAt(i);
        }

        if (arrayOfBooks.length == 0) {
            throw new ItemNotFoundException("no books with titles containing '" +
                                            titlePart + "' were found");
        }
        return arrayOfBooks;
    }

    public static Book getById(int id)
        throws SQLException,
            InstantiationException,
            IllegalAccessException,
            ClassNotFoundException,
            ItemNotFoundException
    {
        BookStoreDB db = getDBInstance();
        String query = "SELECT " + NL_TABLE_BOOK + "." + NL_ID + "," +
                                   NL_TABLE_BOOK + "." + NL_ISBN + "," +
                                   NL_TABLE_BOOK + "." + NL_TITLE + "," +
                                   NL_TABLE_BOOK + "." + NL_YEAR + "," +
                                   NL_TABLE_PUBLISHER + "." + NL_NAME + " as " + NL_PUBLISHER_NAME + "," +
                                   NL_TABLE_BOOK + "." + NL_DESCRIPTION + "," +
                                   NL_TABLE_BOOK + "." + NL_PRICE + "," +
                                   NL_TABLE_BOOK + "." + NL_STOCK +
                       " FROM " +  NL_TABLE_BOOK + "," +
                                   NL_TABLE_AUTHOR + "," +
                                   NL_TABLE_PUBLISHER +
                       " WHERE " + NL_TABLE_BOOK + "." + NL_ID + " = ? AND " +
                                   NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                                   NL_TABLE_AUTHOR + "." + NL_ISBN + " AND " +
                                   NL_TABLE_BOOK + ".publisher_id = " +
                                   NL_TABLE_PUBLISHER + "." + NL_ID;
        PreparedStatement stmt = db.prepareStatement(query,
                                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                                            ResultSet.CONCUR_UPDATABLE);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        boolean exists = rs.first();
        if (!exists) {
            if (addedBooks != null) {
                Enumeration<TempBook> enum_var = addedBooks.elements();
                while (enum_var.hasMoreElements()) {
                    TempBook b = enum_var.nextElement();
                    if (b.getBook().getId() == id) {
                        b.refreshTimestamp();
                        return b.getBook();
                    }
                }
            }
            throw new ItemNotFoundException("no book with the id " + id +
                                            " was found");
        }
        String isbn = rs.getString(NL_ISBN);
        String title = rs.getString(NL_TITLE);
        Date year = rs.getDate(NL_YEAR);
        String publisher = rs.getString(NL_PUBLISHER_NAME);
        String description = rs.getString(NL_DESCRIPTION);
        BigDecimal price = rs.getBigDecimal(NL_PRICE);
        int stock = rs.getInt(NL_STOCK);
        String query2 = "SELECT " + NL_TABLE_AUTHOR + "." + NL_NAME + " as " + NL_AUTHOR_NAME +
                        " FROM " + NL_TABLE_BOOK + "," +
                                   NL_TABLE_AUTHOR + "," +
                                   NL_TABLE_PUBLISHER +
                       " WHERE " + NL_TABLE_BOOK + "." + NL_ID + " = ? AND " +
                                   NL_TABLE_BOOK + "." + NL_ISBN + " = " +
                                   NL_TABLE_AUTHOR + "." + NL_ISBN + " AND " +
                                   NL_TABLE_BOOK + ".publisher_id = " +
                                   NL_TABLE_PUBLISHER + "." + NL_ID;
        PreparedStatement stmt2 = db.prepareStatement(query2,
                                                      ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_UPDATABLE);
        stmt2.setInt(1, id);
        ResultSet rs2 = stmt2.executeQuery();
        boolean more2 = rs2.first();
        Vector<String> authors = new Vector<>();
        while (more2) {
            String author = rs2.getString(NL_AUTHOR_NAME);
            authors.add(author);
            more2 = rs2.next();
        }
        String arr[] = new String[authors.size()];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = authors.elementAt(i);
        }
        stmt.close();
        return new Book(id, isbn, title, year, arr, publisher,
                        description, price, stock);
    }

    public static void addNewItem(TempBook tempbook) throws Exception {
        if (addedBooks == null) {
            addedBooks = new Hashtable<>();
        }
        if (addedBooks.size() >= MAX_BOOKS_TO_ADD) {
            throw new Exception("Too many books (" + MAX_BOOKS_TO_ADD +
                ") have been added already. Added books are removed as soon as the session of the user who added them expires, after 20 minutes of inactivity");
        } else {
            addedBooks.put(new Integer(tempbook.getBook().getId()), tempbook);
        }
    }

    public static synchronized void clearAddedBooks(TempBook tempbook) {
        if (addedBooks != null) {
            Book book = tempbook.getBook();
            addedBooks.remove(book.getId());
        }
    }
}
// END OF FILE