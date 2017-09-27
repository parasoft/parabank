package com.parasoft.parabank.util;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>An interface that describes beans that have a AccessModeController property</DD>
 * <DT>Date:</DT>
 * <DD>Oct 9, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public interface IAccessModeControllerAware {

    /**
     * <DL>
     * <DT>Description:</DT>
     * <DD>A setter for the accessModeController property</DD>
     * <DT>Date:</DT>
     * <DD>Oct 9, 2015</DD>
     * </DL>
     *
     * @param aAccessModeController
     */
    void setAccessModeController(AccessModeController aAccessModeController);
}
