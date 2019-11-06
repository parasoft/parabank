package com.parasoft.parabank.domain.logic.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.parasoft.parabank.dao.NewsDao;
import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.domain.logic.NewsManager;
import com.parasoft.parabank.domain.util.NewsUtil;

/*
 * Implementation of news manager
 */
public class NewsManagerImpl implements NewsManager {

    private final NewsDao newsDao;

    public NewsManagerImpl(final NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.NewsManager#getLatestNews()
     */
    @Override
    public Map<Date, List<News>> getLatestNews() {
        final Date date = newsDao.getLatestNewsDate();
        return NewsUtil.createNewsMap(newsDao.getNewsForDate(date));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.domain.logic.NewsManager#getNews()
     */
    @Override
    public Map<Date, List<News>> getNews() {
        return NewsUtil.createNewsMap(newsDao.getNews());
    }
}
