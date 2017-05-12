package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;

import javax.annotation.*;

import org.junit.*;
import org.springframework.mock.web.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.view.*;

import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.test.util.*;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO add description</DD>
 * <DT>Date:</DT>
 * <DD>Oct 7, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 *
 */
public class InitializeDBControllerTest extends AbstractAdminOperationsTest {
    //private InitializeDBController controller;

    @Resource(name = "adminManager")
    protected AdminManager adminManager;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller = new InitializeDBController();
        //controller.setAdminManager(adminManager);
        setPath("/initializeDB.htm");
        setFormName("none");
        registerSession(request);
    }

    public final void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    @Transactional
    @Commit
    public void testHandleRequest() throws Exception {
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() throws Exception {
                final ModelAndView mav =
                    processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
                assertNotNull(mav);
                assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
                //controller.handleRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
            }
        });
    }

    @Test
    @Transactional
    @Commit
    public void testInitializationError() throws Exception {
        final InitializeDBController controller = //new InitializeDBController();
            getApplicationContext().getBean("/initializeDB.htm", InitializeDBController.class);
        final AdminManager admin = controller.getAdminManager();
        controller.setAdminManager(null);
        final ModelAndView mav = controller.handleRequest();
        assertEquals("error", mav.getViewName());
        controller.setAdminManager(admin);
        // make sure everything back to normal ...
        assertDBInitialized(new DBInitializer() {
            @Override
            public void initializeDB() throws Exception {
                final ModelAndView mav =
                    processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
                assertNotNull(mav);
                assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
                //controller.handleRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
            }
        });
    }
}
