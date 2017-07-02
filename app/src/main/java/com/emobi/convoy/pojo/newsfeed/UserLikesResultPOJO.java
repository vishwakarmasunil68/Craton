package com.emobi.convoy.pojo.newsfeed;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 15-02-2017.
 */

public class UserLikesResultPOJO {
    @SerializedName("like_id")
    String like_id;
    @SerializedName("like_user_id")
    String like_user_id;
    @SerializedName("like_post_id")
    String like_post_id;
    @SerializedName("like_status")
    String like_status;
    @SerializedName("log_name")
    String log_name;
    @SerializedName("log_pics")
    String log_pics;

    public String getLike_id() {
        return like_id;
    }

    public void setLike_id(String like_id) {
        this.like_id = like_id;
    }

    public String getLike_user_id() {
        return like_user_id;
    }

    public void setLike_user_id(String like_user_id) {
        this.like_user_id = like_user_id;
    }

    public String getLike_post_id() {
        return like_post_id;
    }

    public void setLike_post_id(String like_post_id) {
        this.like_post_id = like_post_id;
    }

    public String getLike_status() {
        return like_status;
    }

    public void setLike_status(String like_status) {
        this.like_status = like_status;
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
        return "UserLikesResultPOJO{" +
                "like_id='" + like_id + '\'' +
                ", like_user_id='" + like_user_id + '\'' +
                ", like_post_id='" + like_post_id + '\'' +
                ", like_status='" + like_status + '\'' +
                ", log_name='" + log_name + '\'' +
                ", log_pics='" + log_pics + '\'' +
                '}';
    }
}
