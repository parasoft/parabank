package com.parasoft.parabank.web.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.logic.*;
import com.parasoft.parabank.util.*;

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
