package com.parasoft.parabank.web.controller;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.util.Constants;

@SuppressWarnings("unchecked")
public class NewsControllerTest extends AbstractControllerTest<NewsController> {
    //    @Resource(name = "newsManager")
    //    private NewsManager newsManager;

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        setPath("/news.htm");
        setFormName("none");
        //registerSession(request);
        //controller.setNewsManager(newsManager);
    }

    //    public void setNewsManager(final NewsManager newsManager) {
    //        this.newsManager = newsManager;
    //    }

    @Test
    public void testHandleRequest() throws Exception {
        //final ModelAndView mav = controller.handleRequest(request, response);
        final ModelAndView mav = processGetRequest(request, new MockHttpServletResponse());

        assertEquals(Constants.NEWS, mav.getViewName());

        final Map<Date, List<News>> news = (Map<Date, List<News>>) getModelValue(mav, Constants.NEWS);
        assertEquals(4, news.size());
    }
}
