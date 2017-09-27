package com.parasoft.parabank.domain.logic;

import java.util.*;

import com.parasoft.parabank.domain.*;

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
