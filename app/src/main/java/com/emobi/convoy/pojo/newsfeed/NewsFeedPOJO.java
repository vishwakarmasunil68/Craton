package com.emobi.convoy.pojo.newsfeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 15-02-2017.
 */

public class NewsFeedPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<NewsFeedPostResultPOJO> list_pojo;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<NewsFeedPostResultPOJO> getList_pojo() {
        return list_pojo;
    }

    public void setList_pojo(List<NewsFeedPostResultPOJO> list_pojo) {
        this.list_pojo = list_pojo;
    }

    @Override
    public String toString() {
        return "NewsFeedPOJO{" +
                "success='" + success + '\'' +
                ", list_pojo=" + list_pojo +
                '}';
    }
}
