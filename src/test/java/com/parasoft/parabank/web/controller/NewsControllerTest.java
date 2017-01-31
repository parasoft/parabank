package com.parasoft.parabank.web.controller;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

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
        assertThat(news.size(), anyOf(equalTo(3), equalTo(4)));  // 3 dates if January 31st
        int numNews = 0;
        for (Map.Entry<Date, List<News>> entry : news.entrySet()) {
            numNews += entry.getValue().size();
        }
        assertEquals(6, numNews);
    }
}
