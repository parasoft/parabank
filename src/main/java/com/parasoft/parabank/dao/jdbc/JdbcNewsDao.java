package com.parasoft.parabank.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.parasoft.parabank.dao.NewsDao;
import com.parasoft.parabank.domain.News;

/*
 * JDBC implementation of NewsDao
 */
public class JdbcNewsDao extends JdbcDaoSupport implements NewsDao {
    private static final class NewsMapper implements RowMapper<News> {
        @Override
        public News mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final News news = new News();
            news.setId(rs.getInt("id"));
            news.setDate(rs.getDate("date"));
            news.setHeadline(rs.getString("headline"));
            news.setStory(rs.getString("story"));
            return news;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(JdbcNewsDao.class);

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.NewsDao#getLatestNewsDate()
     */
    @Override
    public Date getLatestNewsDate() {
        final String SQL = "SELECT MAX(date) FROM News";

        return getJdbcTemplate().queryForObject(SQL, java.sql.Date.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.NewsDao#getNews()
     */
    @Override
    public List<News> getNews() {
        final String SQL = "SELECT id, date, headline, story FROM News ORDER BY id DESC";

        final List<News> news = getJdbcTemplate().query(SQL, new NewsMapper());
        log.info("Retrieved " + news.size() + " news items");

        return news;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.parasoft.parabank.dao.NewsDao#getNewsForDate(java.util.Date)
     */
    @Override
    public List<News> getNewsForDate(final Date date) {
        final String SQL = "SELECT id, date, headline, story FROM News WHERE date = ? ORDER BY id DESC";

        final List<News> news = getJdbcTemplate().query(SQL, new NewsMapper(), date);
        log.info("Retrieved " + news.size() + " news items for date = " + date);

        return news;
    }
}
