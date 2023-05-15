package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.domain.logic.AdminManager;
import com.parasoft.parabank.test.util.AbstractAdminOperationsTest;

/**
 * @req PAR-7
 * @req PAR-32
 * @req PAR-43
 * @req PAR-21
 *
 */
public class JmsListenerControllerTest extends AbstractAdminOperationsTest { //AbstractControllerTest<JmsListenerController> {
    @Resource(name = "adminManager")
    private AdminManager adminManager;

    // @Resource(name = "jmsListener")
    // private MockJmsListeningContainer jmsListener;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setAdminManager(adminManager);
        adminManager.setJmsListener(getJmsListener());
        setPath("/jms.htm");
        setFormName("none");
        registerSession(request);

    }

    public void setAdminManager(final AdminManager adminManager) {
        this.adminManager = adminManager;
    }

    //   public void setJmsListener(final MockJmsListeningContainer jmsListener) {
    //       this.jmsListener = jmsListener;
    //   }

    @Test
    public void testHandleBadRequest() throws Exception {
        //final ModelAndView mav = controller.handleRequest(request, response);
        try {
            processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
            fail("expected exception (MissingServletRequestParameterException) not thrown");
        } catch (final Exception ex) {
            assertEquals("Required request parameter 'shutdown' for method parameter type String is not present", ex.getMessage());
            // this is good
        }
        final MockHttpServletRequest req = registerSession(new MockHttpServletRequest());
        req.setParameter("shutdown", "");
        final ModelAndView mav = processGetRequest(req, new MockHttpServletResponse());
        assertNotNull(mav);
        // assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());
        assertEquals("/admin.htm", ((RedirectView) mav.getView()).getUrl());
        assertEquals("error.empty.shutdown.parameter", mav.getModel().get("error"));
    }

    @Test
    public void testHandleShutdown() throws Exception {
        getJmsListener().setListenerRunning(true);
        getJmsListener().setListenerInitialized(true);
        final JmsListenerController jlc = getApplicationContext().getBean("/jms.htm", JmsListenerController.class);
        assertNotNull(jlc);
        jlc.setAdminManager(adminManager);
        final MockHttpServletRequest req = registerSession(new MockHttpServletRequest());
        req.setParameter("shutdown", "true");
        //final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processGetRequest(req, new MockHttpServletResponse());
        assertNotNull(mav);
        //assertNull(mav);
        //assertEquals("admin.htm", response.getRedirectedUrl());
        assertEquals("/admin.htm", ((RedirectView) mav.getView()).getUrl());
        assertFalse(getJmsListener().isListenerRunning());
        assertFalse(getJmsListener().isListenerInitialized());
    }

    @Test
    public void testHandleStartup() throws Exception {
        getJmsListener().setListenerRunning(false);
        getJmsListener().setListenerInitialized(false);
        final JmsListenerController jlc = getApplicationContext().getBean("/jms.htm", JmsListenerController.class);
        assertNotNull(jlc);
        jlc.setAdminManager(adminManager);
        final MockHttpServletRequest req = registerSession(new MockHttpServletRequest());
        req.setParameter("shutdown", "false");
        final ModelAndView mav = processGetRequest(req, new MockHttpServletResponse());
        assertNotNull(mav);
        //final ModelAndView mav = controller.handleRequest(request, response);
        //assertNull(mav);
        //assertEquals("admin.htm", response.getRedirectedUrl());
        assertEquals("/admin.htm", ((RedirectView) mav.getView()).getUrl());
        assertTrue(getJmsListener().isListenerRunning());
        assertTrue(getJmsListener().isListenerInitialized());
    }
}
