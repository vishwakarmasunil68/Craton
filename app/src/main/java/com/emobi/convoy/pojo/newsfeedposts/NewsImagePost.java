package com.emobi.convoy.pojo.newsfeedposts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 08-05-2017.
 */

public class NewsImagePost {
    @SerializedName("post_id")
    String post_id;
    @SerializedName("post_user_id")
    String post_user_id;
    @SerializedName("post_cat_id")
    String post_cat_id;
    @SerializedName("post_msg")
    String post_msg;
    @SerializedName("post_image")
    String post_image;
    @SerializedName("post_time")
    String post_time;
    @SerializedName("post_date")
    String post_date;
    @SerializedName("post_datetime")
    String post_datetime;
    @SerializedName("post_likes")
    String post_likes;
    @SerializedName("post_image_status")
    String post_image_status;
    @SerializedName("post_share")
    String post_share;
    @SerializedName("share_user_name")
    String share_user_name;
    @SerializedName("log_name")
    String log_name;
    @SerializedName("log_pics")
    String log_pics;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_user_id() {
        return post_user_id;
    }

    public void setPost_user_id(String post_user_id) {
        this.post_user_id = post_user_id;
    }

    public String getPost_cat_id() {
        return post_cat_id;
    }

    public void setPost_cat_id(String post_cat_id) {
        this.post_cat_id = post_cat_id;
    }

    public String getPost_msg() {
        return post_msg;
    }

    public void setPost_msg(String post_msg) {
        this.post_msg = post_msg;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_datetime() {
        return post_datetime;
    }

    public void setPost_datetime(String post_datetime) {
        this.post_datetime = post_datetime;
    }

    public String getPost_likes() {
        return post_likes;
    }

    public void setPost_likes(String post_likes) {
        this.post_likes = post_likes;
    }

    public String getPost_image_status() {
        return post_image_status;
    }

    public void setPost_image_status(String post_image_status) {
        this.post_image_status = post_image_status;
    }

    public String getPost_share() {
        return post_share;
    }

    public void setPost_share(String post_share) {
        this.post_share = post_share;
    }

    public String getShare_user_name() {
        return share_user_name;
    }

    public void setShare_user_name(String share_user_name) {
        this.share_user_name = share_user_name;
    }

    public String getLog_name() {
        return log_name;
    }

    public void setLog_name(String log_name) {
        this.log_name = log_name;
    }

    public String getLog_pics() {
        return log_pics;
    }

    public void setLog_pics(String log_pics) {
        this.log_pics = log_pics;
    }

    @Override
    public String toString() {
        return "NewsImagePost{" +
                "post_id='" + post_id + '\'' +
                ", post_user_id='" + post_user_id + '\'' +
                ", post_cat_id='" + post_cat_id + '\'' +
                ", post_msg='" + post_msg + '\'' +
                ", post_image='" + post_image + '\'' +
                ", post_time='" + post_time + '\'' +
                ", post_date='" + post_date + '\'' +
                ", post_datetime='" + post_datetime + '\'' +
                ", post_likes='" + post_likes + '\'' +
                ", post_image_status='" + post_image_status + '\'' +
                ", post_share='" + post_share + '\'' +
                ", share_user_name='" + share_user_name + '\'' +
                ", log_name='" + log_name + '\'' +
                ", log_pics='" + log_pics + '\'' +
                '}';
    }
}
