package com.emobi.convoy.pojo.singlenewsfeed;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 08-05-2017.
 */

public class SingleCommentResultPOJO {

    @SerializedName("comment_id")
    private String commentId;
    @SerializedName("comment_user_id")
    private String commentUserId;
    @SerializedName("comment_post_id")
    private String commentPostId;
    @SerializedName("comment_msg")
    private String commentMsg;
    @SerializedName("date")
    private String date;
    @SerializedName("time")
    private String time;
    @SerializedName("log_name")
    private String logName;
    @SerializedName("log_pics")
    private String logPics;

    public SingleCommentResultPOJO(String commentId, String commentUserId, String commentPostId, String commentMsg, String date, String time, String logName, String logPics) {
        this.commentId = commentId;
        this.commentUserId = commentUserId;
        this.commentPostId = commentPostId;
        this.commentMsg = commentMsg;
        this.date = date;
        this.time = time;
        this.logName = logName;
        this.logPics = logPics;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentPostId() {
        return commentPostId;
    }

    public void setCommentPostId(String commentPostId) {
        this.commentPostId = commentPostId;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
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

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogPics() {
        return logPics;
    }

    public void setLogPics(String logPics) {
        this.logPics = logPics;
    }

    @Override
    public String toString() {
        return "SingleCommentResultPOJO{" +
                "commentId='" + commentId + '\'' +
                ", commentUserId='" + commentUserId + '\'' +
                ", commentPostId='" + commentPostId + '\'' +
                ", commentMsg='" + commentMsg + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", logName='" + logName + '\'' +
                ", logPics='" + logPics + '\'' +
                '}';
    }
}
