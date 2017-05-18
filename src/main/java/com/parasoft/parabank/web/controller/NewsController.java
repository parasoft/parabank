package com.parasoft.parabank.web.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.parasoft.parabank.domain.*;
import com.parasoft.parabank.domain.logic.impl.*;
import com.parasoft.parabank.util.*;

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
            ret.sort(new Comparator<News>() {
                @Override
                public int compare(News o1, News o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            while (ret.size() > count) {
                ret.remove(ret.size()-1);
            }
        }
        return new ResponseEntity<List<News>>(ret, HttpStatus.OK);
    }
}
