package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.parasoft.parabank.dao.NewsDao;
import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

/**
 * @req PAR-14
 *
 */
public class JdbcNewsDaoTest extends AbstractParaBankDataSourceTest {
    @Resource(name = "newsDao")
    private NewsDao newsDao;

    public void setNewsDao(final NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Test
    public void testGetNews() {
        final List<News> news = newsDao.getNews();
        assertEquals(6, news.size());
    }

    @Test
    public void testGetNewsForDate() {
        //@SuppressWarnings("deprecation")
        final Date today = new Date(new java.util.Date().getTime());
        final List<News> news = newsDao.getNewsForDate(today);
        assertEquals(3, news.size());
        final News item = news.get(0);
        assertEquals(6, item.getId());
        assertEquals(today.toString(), item.getDate().toString());
        assertEquals("ParaBank Is Now Re-Opened", item.getHeadline());
        assertNotNull(item.getStory());
    }

    @Test
    public void testLastestNewsDate() {
        final Date today = new Date(new java.util.Date().getTime());
        assertEquals(today.toString(), newsDao.getLatestNewsDate().toString());
    }
}
