package com.parasoft.parabank.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.logic.NewsManager;
import com.parasoft.parabank.util.Constants;

/**
 * Controller for displaying news page
 */
@Controller("/news.htm")
@RequestMapping("/news.htm")
public class NewsController {

    @Autowired
    @Qualifier("newsManager")
    private NewsManager newsManager;

    @GetMapping
    public ModelAndView handleRequest(Model model) throws Exception {
        model.addAttribute(Constants.NEWS, newsManager.getNews());
        return new ModelAndView(Constants.NEWS, "model", model.asMap());
    }
}
