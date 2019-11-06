package com.parasoft.parabank.domain;

import java.util.Date;

import com.parasoft.parabank.util.Util;

/**
 * Domain object representing a bank news item
 */
public class News implements Comparable<News> {
    private int id;
    private Date date;
    private String headline;
    private String story;

    public News() {
        // Do nothing by default
    }

    public News(int id, Date date, String headline, String story) {
        this.id = id;
        this.date = date;
        this.headline = headline;
        this.story = story;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + (date == null ? 0 : date.hashCode());
        result = prime * result + (headline == null ? 0 : headline.hashCode());
        result = prime * result + (story == null ? 0 : story.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof News)) {
            return false;
        }
        News other = (News)obj;
        return id == other.id &&
            Util.equals(date, other.date) &&
            Util.equals(headline, other.headline) &&
            Util.equals(story, other.story);
    }

    @Override
    public String toString() {
        return "News [id=" + id + ", date=" + date + ", headline=" + headline
                + ", story=" + story + "]";
    }

    @Override
    public int compareTo(News o) {
        return date.compareTo(o.date);
    }
}
