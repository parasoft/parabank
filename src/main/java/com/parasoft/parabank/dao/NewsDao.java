package com.parasoft.parabank.dao;

import java.util.Date;
import java.util.List;

import com.parasoft.parabank.domain.News;

/**
 * Interface for accessing bank news items from a data source
 */
public interface NewsDao {

    /**
     * Retrieve list of all bank news items
     *
     * @return list of news items
     */
    List<News> getNews();

    /**
     * Retrieve list of bank news items for a given date
     *
     * @param date the date to list
     * @return list of news items for the given date
     */
    List<News> getNewsForDate(Date date);

    /**
     * Retrieve the date of the most recent news item
     *
     * @return date of most recent item
     */
    Date getLatestNewsDate();
}
