package com.parasoft.parabank.domain.util;

import java.util.*;

import org.slf4j.*;

import com.parasoft.parabank.domain.*;

public class NewsUtil {
    private static final Logger log = LoggerFactory.getLogger(NewsUtil.class);

    private NewsUtil() {
        // static util methods only
    }

    /*
     * Convert list of news to a map of news items grouped by common dates
     */
    public static Map<Date, List<News>> createNewsMap(final News... news) {
        return createNewsMap(Arrays.asList(news));
    }

    /*
     * Convert list of news to a map of news items grouped by common dates
     */
    public static Map<Date, List<News>> createNewsMap(final List<News> news) {
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
}
