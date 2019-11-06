package com.parasoft.parabank.web.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.domain.logic.impl.NewsManagerImpl;
import com.parasoft.parabank.util.Constants;

/**
 * Controller for displaying news page
 */
@Controller("/news.htm")
@RequestMapping("/news.htm")
public class NewsController {

    @Autowired
    @Qualifier("newsManager")
    private NewsManagerImpl newsManager;

    @GetMapping
    public ModelAndView handleRequest(Model model) throws Exception {
        model.addAttribute(Constants.NEWS, newsManager.getNews());
        return new ModelAndView(Constants.NEWS, "model", model.asMap());
    }

    @GetMapping("/{count}")
    public ResponseEntity<List<News>> getRecent(@PathVariable("count") int count) throws Exception {
        Map<Date, List<News>> news = newsManager.getLatestNews();
        List<News> ret = null;
        for (List<News> list : news.values()) {
            if (!list.isEmpty()) {
                ret = list;
                break;
            }
        }
        if (ret != null) {
            // Sort most recent first and truncate size to count
            ret.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
            while (ret.size() > count) {
                ret.remove(ret.size()-1);
            }
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
