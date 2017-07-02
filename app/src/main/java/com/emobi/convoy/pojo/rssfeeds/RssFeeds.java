package com.emobi.convoy.pojo.rssfeeds;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 05-04-2017.
 */

public class RssFeeds {
    @SerializedName("status")
    String status;
    @SerializedName("source")
    String source;
    @SerializedName("sortBy")
    String sortBy;
    @SerializedName("articles")
    List<Articles> articlesList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<Articles> getArticlesList() {
        return articlesList;
    }

    public void setArticlesList(List<Articles> articlesList) {
        this.articlesList = articlesList;
    }

    @Override
    public String toString() {
        return "RssFeeds{" +
                "status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", articlesList=" + articlesList +
                '}';
    }
}
