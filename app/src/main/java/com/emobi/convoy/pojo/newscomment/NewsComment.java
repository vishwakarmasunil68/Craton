package com.emobi.convoy.pojo.newscomment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 07-04-2017.
 */

public class NewsComment {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<NewsCommentResultPOJO> newsCommentResultPOJOList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<NewsCommentResultPOJO> getNewsCommentResultPOJOList() {
        return newsCommentResultPOJOList;
    }

    public void setNewsCommentResultPOJOList(List<NewsCommentResultPOJO> newsCommentResultPOJOList) {
        this.newsCommentResultPOJOList = newsCommentResultPOJOList;
    }

    @Override
    public String toString() {
        return "NewsComment{" +
                "success='" + success + '\'' +
                ", newsCommentResultPOJOList=" + newsCommentResultPOJOList.toString() +
                '}';
    }
}
