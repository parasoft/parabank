package com.parasoft.parabank.domain.logic.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parasoft.parabank.dao.NewsDao;
import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.domain.logic.NewsManager;

/*
 * Implementation of news manager
 */
public class NewsManagerImpl implements NewsManager {
    private static final Logger log = LoggerFactory.getLogger(NewsManagerImpl.class);

    private NewsDao newsDao;

    /*
     * Convert list of news to a map of news items grouped by common dates
     */
    private Map<Date, List<News>> createNewsMap(final List<News> news) {
        final Map<Date, List<News>> newsMap = new LinkedHashMap<Date, List<News>>();
        for (final News item : news) {
            final Date date = item.getDate();
            if (!newsMap.containsKey(date)) {
                log.info("Creating new list for news date: " + date);
                newsMap.put(date, new ArrayList<News>());
            }
            final List<News> newsList = newsMap.get(date);
            newsList.add(item);
            log.info("Adding news item with id = " + item.getId());
        }
        return newsMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.parasoft.parabank.domain.logic.NewsManager#getLatestNews()
     */
    @Override
    public Map<Date, List<News>> getLatestNews() {
        final Date date = newsDao.getLatestNewsDate();
        return createNewsMap(newsDao.getNewsForDate(date));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.parasoft.parabank.domain.logic.NewsManager#getNews()
     */
    @Override
    public Map<Date, List<News>> getNews() {
        return createNewsMap(newsDao.getNews());
    }

    public void setNewsDao(final NewsDao newsDao) {
        this.newsDao = newsDao;
    }
}
