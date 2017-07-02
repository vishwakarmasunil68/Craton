package com.emobi.convoy.pojo.newsfeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 15-02-2017.
 */

public class NewsFeedPostResultPOJO {
    @SerializedName("log_pics")
    String log_pics;
    @SerializedName("log_name")
    String log_name;
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
    @SerializedName("post_comment")
    String post_comment;
    @SerializedName("user_likes_status")
    String user_likes_status;
    @SerializedName("user_likes")
    List<UserLikesResultPOJO> userLikesResultPOJOList;
    @SerializedName("user_comment_status")
    String user_comment_status;
    @SerializedName("user_comment")
    List<UserCommentResultPOJO> userCommentResultPOJOList;
    @SerializedName("user_share")
    String user_share;
    @SerializedName("share_user_name")
    String share_user_name;


    public String getLog_pics() {
        return log_pics;
    }

    public void setLog_pics(String log_pics) {
        this.log_pics = log_pics;
    }

    public String getLog_name() {
        return log_name;
    }

    public void setLog_name(String log_name) {
        this.log_name = log_name;
    }

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

    public String getPost_comment() {
        return post_comment;
    }

    public void setPost_comment(String post_comment) {
        this.post_comment = post_comment;
    }

    public String getUser_likes_status() {
        return user_likes_status;
    }

    public void setUser_likes_status(String user_likes_status) {
        this.user_likes_status = user_likes_status;
    }

    public List<UserLikesResultPOJO> getUserLikesResultPOJOList() {
        return userLikesResultPOJOList;
    }

    public void setUserLikesResultPOJOList(List<UserLikesResultPOJO> userLikesResultPOJOList) {
        this.userLikesResultPOJOList = userLikesResultPOJOList;
    }

    public String getUser_comment_status() {
        return user_comment_status;
    }

    public void setUser_comment_status(String user_comment_status) {
        this.user_comment_status = user_comment_status;
    }

    public List<UserCommentResultPOJO> getUserCommentResultPOJOList() {
        return userCommentResultPOJOList;
    }

    public void setUserCommentResultPOJOList(List<UserCommentResultPOJO> userCommentResultPOJOList) {
        this.userCommentResultPOJOList = userCommentResultPOJOList;
    }

    public String getUser_share() {
        return user_share;
    }

    public void setUser_share(String user_share) {
        this.user_share = user_share;
    }

    public String getShare_user_name() {
        return share_user_name;
    }

    public void setShare_user_name(String share_user_name) {
        this.share_user_name = share_user_name;
    }

    @Override
    public String toString() {
        return "NewsFeedPostResultPOJO{" +
                "log_pics='" + log_pics + '\'' +
                ", log_name='" + log_name + '\'' +
                ", post_id='" + post_id + '\'' +
                ", post_user_id='" + post_user_id + '\'' +
                ", post_cat_id='" + post_cat_id + '\'' +
                ", post_msg='" + post_msg + '\'' +
                ", post_image='" + post_image + '\'' +
                ", post_time='" + post_time + '\'' +
                ", post_date='" + post_date + '\'' +
                ", post_datetime='" + post_datetime + '\'' +
                ", post_likes='" + post_likes + '\'' +
                ", post_image_status='" + post_image_status + '\'' +
                ", post_comment='" + post_comment + '\'' +
                ", user_likes_status='" + user_likes_status + '\'' +
                ", userLikesResultPOJOList=" + userLikesResultPOJOList +
                ", user_comment_status='" + user_comment_status + '\'' +
                ", userCommentResultPOJOList=" + userCommentResultPOJOList +
                ", user_share='" + user_share + '\'' +
                ", share_user_name='" + share_user_name + '\'' +
                '}';
    }
}
