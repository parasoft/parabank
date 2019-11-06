package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.domain.logic.NewsManager;
import com.parasoft.parabank.util.Constants;

/**
 * @req PAR-5
 * @req PAR-6
 * @req PAR-8
 * @req PAR-9
 * @req PAR-10
 */
@SuppressWarnings({ "unchecked" })
public class IndexControllerTest extends AbstractControllerTest<IndexController> {
    @Resource(name = "newsManager")
    private NewsManager newsManager;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        //controller.setNewsManager(newsManager);
        setPath("/index.htm");
        setFormName("none");
        registerSession(request);
    }

    public void setNewsManager(final NewsManager newsManager) {
        this.newsManager = newsManager;
    }

    @Test
    @Transactional
    @Commit
    public void testDatabaseUninitialized() throws Exception {
        getJdbcTemplate().execute("DROP TABLE News");
        ModelAndView mav =
                processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());

        //final ModelAndView mav = controller.handleRequest(request, response);
        assertNotNull(mav);
        assertEquals("/initializeDB.htm", ((RedirectView) mav.getView()).getUrl());
        setPath("/initializeDB.htm");
        setFormName("none");
        mav = processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        assertNotNull(mav);
        assertEquals("/index.htm", ((RedirectView) mav.getView()).getUrl());

    }

    @Test
    public void testHandleGetRequest() throws Exception {
        final Calendar c = Calendar.getInstance();
        final java.sql.Date today = new java.sql.Date(c.getTimeInMillis());

        final ModelAndView mav =
                processGetRequest(registerSession(new MockHttpServletRequest()), new MockHttpServletResponse());
        //        final ModelAndView mav = controller.handleRequest(request, response);

        assertEquals(Constants.INDEX, mav.getViewName());

        final Date date = (Date) getModelValue(mav, "date");
        assertEquals(today.toString(), date.toString());

        final List<News> news = (List<News>) getModelValue(mav, Constants.NEWS);
        assertEquals(3, news.size());
    }
}
