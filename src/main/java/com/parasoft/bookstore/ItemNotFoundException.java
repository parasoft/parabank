package com.parasoft.bookstore;

public class ItemNotFoundException extends Exception {
    /**
     * <DL><DT>serialVersionUID</DT> <DD>Default serial version UID</DD></DL>
     */
    private static final long serialVersionUID = 1L;
    public ItemNotFoundException(String msg) {
        super(msg);
    }
}
