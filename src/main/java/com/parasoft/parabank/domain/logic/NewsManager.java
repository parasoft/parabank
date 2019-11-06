package com.parasoft.parabank.domain.logic;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.parasoft.parabank.domain.News;

/**
 * Interface for bank news management
 */
public interface NewsManager {

    /**
     * Retrieve map of latest bank news items
     *
     * @return map of latest bank news items keyed by last news date
     */
    Map<Date, List<News>> getLatestNews();

    /**
     * Retrieve map of all bank news items
     *
     * @return map of news items keyed by news dates
     */
    Map<Date, List<News>> getNews();
}
