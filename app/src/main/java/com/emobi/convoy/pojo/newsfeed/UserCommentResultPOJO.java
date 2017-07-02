package com.emobi.convoy.pojo.newsfeed;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 15-02-2017.
 */

public class UserCommentResultPOJO {
    @SerializedName("comment_id")
    String comment_id;
    @SerializedName("comment_user_id")
    String comment_user_id;
    @SerializedName("comment_post_id")
    String comment_post_id;
    @SerializedName("comment_msg")
    String comment_msg;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("log_name")
    String log_name;
    @SerializedName("log_pics")
    String log_pics;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(String comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_post_id() {
        return comment_post_id;
    }

    public void setComment_post_id(String comment_post_id) {
        this.comment_post_id = comment_post_id;
    }

    public String getComment_msg() {
        return comment_msg;
    }

    public void setComment_msg(String comment_msg) {
        this.comment_msg = comment_msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        return "UserCommentResultPOJO{" +
                "comment_id='" + comment_id + '\'' +
                ", comment_user_id='" + comment_user_id + '\'' +
                ", comment_post_id='" + comment_post_id + '\'' +
                ", comment_msg='" + comment_msg + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", log_name='" + log_name + '\'' +
                ", log_pics='" + log_pics + '\'' +
                '}';
    }
}
