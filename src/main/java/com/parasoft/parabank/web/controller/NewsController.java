package com.parasoft.parabank.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.logic.NewsManager;
import com.parasoft.parabank.util.AccessModeController;
import com.parasoft.parabank.util.Constants;

/**
 * Controller for displaying news page
 */
@Controller("/news.htm")
@RequestMapping("/news.htm")
public class NewsController extends AbstractBankController {

    @Resource(name = "newsManager")
    private NewsManager newsManager;

    @RequestMapping
    public ModelAndView handleRequest() throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(Constants.NEWS, newsManager.getNews());
        return new ModelAndView(Constants.NEWS, "model", model);
    }

    @Override
    public void setAccessModeController(final AccessModeController aAccessModeController) {
        // not implemented

    }

    /** {@inheritDoc} */
    @Override
    @Resource(name = Constants.NEWS)
    public void setFormView(final String aFormView) {
        super.setFormView(aFormView);
    }

    //    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //        Map<String, Object> model = new HashMap<String, Object>();
    //        model.put(Constants.NEWS, newsManager.getNews());
    //        return new ModelAndView(Constants.NEWS, "model", model);
    //    }
    public void setNewsManager(final NewsManager newsManager) {
        this.newsManager = newsManager;
    }
}
