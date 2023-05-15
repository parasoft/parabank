package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

/**
 * <DL>
 * <DT>Description:</DT>
 * <DD>TODO add description</DD>
 * <DT>Date:</DT>
 * <DD>Oct 7, 2015</DD>
 * </DL>
 *
 * @author nrapo - Nick Rapoport
 * @req PAR-5
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
        assertDBInitialized(() -> {
            final ModelAndView mav =
                processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
            assertNotNull(mav);
            assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
            //controller.handleRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
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
        assertDBInitialized(() -> {
            final ModelAndView mav1 =
                processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
            assertNotNull(mav1);
            assertEquals("/index.htm", ((RedirectView) mav1.getView()).getUrl());
            //controller.handleRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
        });
    }
}
