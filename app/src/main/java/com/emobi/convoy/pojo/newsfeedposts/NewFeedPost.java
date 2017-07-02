package com.emobi.convoy.pojo.newsfeedposts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 08-05-2017.
 */

public class NewFeedPost {
    @SerializedName("success")
    String success;
    @SerializedName("image_data_success")
    String image_data_success;
    @SerializedName("image_data_result")
    List<NewsImagePost> image_data_result;
    @SerializedName("text_data_success")
    String text_data_success;
    @SerializedName("text_data_result")
    List<NewsTextPost> newsTextPostList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getImage_data_success() {
        return image_data_success;
    }

    public void setImage_data_success(String image_data_success) {
        this.image_data_success = image_data_success;
    }

    public List<NewsImagePost> getImage_data_result() {
        return image_data_result;
    }

    public void setImage_data_result(List<NewsImagePost> image_data_result) {
        this.image_data_result = image_data_result;
    }

    public String getText_data_success() {
        return text_data_success;
    }

    public void setText_data_success(String text_data_success) {
        this.text_data_success = text_data_success;
    }

    public List<NewsTextPost> getNewsTextPostList() {
        return newsTextPostList;
    }

    public void setNewsTextPostList(List<NewsTextPost> newsTextPostList) {
        this.newsTextPostList = newsTextPostList;
    }

    @Override
    public String toString() {
        return "NewFeedPost{" +
                "success='" + success + '\'' +
                ", image_data_success='" + image_data_success + '\'' +
                ", image_data_result=" + image_data_result +
                ", text_data_success='" + text_data_success + '\'' +
                ", newsTextPostList=" + newsTextPostList +
                '}';
    }
}
