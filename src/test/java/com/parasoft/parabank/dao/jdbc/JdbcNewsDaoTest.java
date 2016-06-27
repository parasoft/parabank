package com.parasoft.parabank.dao.jdbc;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.parasoft.parabank.dao.NewsDao;
import com.parasoft.parabank.domain.News;
import com.parasoft.parabank.test.util.AbstractParaBankDataSourceTest;

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
        @SuppressWarnings("deprecation")
        final List<News> news = newsDao.getNewsForDate(new Date(110, 8, 13));
        assertEquals(3, news.size());
        final News item = news.get(0);
        assertEquals(6, item.getId());
        assertEquals("2010-09-13", item.getDate().toString());
        assertEquals("ParaBank Is Now Re-Open", item.getHeadline());
        assertNotNull(item.getStory());
    }

    @Test
    public void testLastestNewsDate() {
        assertEquals("2010-09-13 00:00:00.0", newsDao.getLatestNewsDate().toString());
    }
}
