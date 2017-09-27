package com.parasoft.parabank.service;

/**
 * Default class for service errors
 */
public class ParaBankServiceException extends Exception {
    /**
     * <DL>
     * <DT>serialVersionUID</DT>
     * <DD>default serialVersionUID</DD>
     * </DL>
     */
    private static final long serialVersionUID = 1L;

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>ParaBankServiceException Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     */
    public ParaBankServiceException() {
        super();
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>ParaBankServiceException Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param message
     */
    public ParaBankServiceException(final String message) {
        super(message);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>ParaBankServiceException Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param aMessage
     * @param aCause
     */
    public ParaBankServiceException(final String aMessage, final Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>ParaBankServiceException Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param aMessage
     * @param aCause
     * @param aEnableSuppression
     * @param aWritableStackTrace
     */
    public ParaBankServiceException(final String aMessage, final Throwable aCause, final boolean aEnableSuppression,
            final boolean aWritableStackTrace) {
        super(aMessage, aCause, aEnableSuppression, aWritableStackTrace);
    }

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>ParaBankServiceException Constructor</DD>
     * <DT>Date:</DT>
     * <DD>Oct 10, 2015</DD>
     * </DL>
     *
     * @param aCause
     */
    public ParaBankServiceException(final Throwable aCause) {
        super(aCause);
    }
}
