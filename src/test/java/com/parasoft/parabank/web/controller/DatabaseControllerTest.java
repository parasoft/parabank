package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

/**
 * @req PAR-12
 * @req PAR-22
 * @req PAR-33
 * @req PAR-44
 *
 */
public class DatabaseControllerTest extends AbstractAdminOperationsTest {
    //    @Resource(name = "databaseController")
    //    private DatabaseController controller;

    @Resource(name = "adminManager")
    protected AdminManager adminManager;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller = new DatabaseController();
        //controller.setAdminManager(adminManager);
        setPath("/db.htm");
        setFormName("none");
        //registerSession(request);

    }

    public final void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    @Test
    @Transactional
    @Commit
    public void testHandleRequest() throws Exception {
        try {
            processPostRequest(null, new MockHttpServletRequest(), new MockHttpServletResponse());
            fail("expected exception (MissingServletRequestParameterException) not thrown");
        } catch (final Exception ex) {
            assertEquals("Required request parameter 'action' for method parameter type String is not present", ex.getMessage());
            // this is good
        }
        MockHttpServletRequest lRequest = new MockHttpServletRequest();
        lRequest.setParameter("action", "");
        ModelAndView mav = processPostRequest(null, lRequest, new MockHttpServletResponse());
        assertEquals("admin", mav.getViewName());
        assertEquals("error.invalid.action.parameter", mav.getModel().get("error"));

        lRequest = new MockHttpServletRequest();
        //        ModelAndView mav = controller.handleRequest(request, new MockHttpServletResponse());
        //        assertEquals("error", mav.getViewName());

        lRequest.setParameter("action", "unknown");
        //        mav = controller.handleRequest(lRequest, new MockHttpServletResponse());
        mav = processPostRequest(null, lRequest, new MockHttpServletResponse());
        assertEquals("admin", mav.getViewName());
        assertEquals("error.invalid.action.parameter", mav.getModel().get("error"));

        assertDBClean(() -> {
            final MockHttpServletRequest lRequest1 = new MockHttpServletRequest();
            lRequest1.setParameter("action", "CLEAN");
            processPostRequest(null, lRequest1, new MockHttpServletResponse());
            //controller.handleRequest(request, new MockHttpServletResponse());
        });
        assertDBInitialized(() -> {
            final MockHttpServletRequest lRequest1 = new MockHttpServletRequest();
            lRequest1.setParameter("action", "INIT");
            processPostRequest(null, lRequest1, new MockHttpServletResponse());

            //controller.handleRequest(request, new MockHttpServletResponse());
        });
    }
}
