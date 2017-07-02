package com.emobi.convoy.pojo.singlenewsfeed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 08-05-2017.
 */

public class SingleNewsFeedResultPOJO {
    @SerializedName("post_id")
    private String postId;
    @SerializedName("post_user_id")
    private String postUserId;
    @SerializedName("post_cat_id")
    private String postCatId;
    @SerializedName("post_msg")
    private String postMsg;
    @SerializedName("post_image")
    private String postImage;
    @SerializedName("post_time")
    private String postTime;
    @SerializedName("post_date")
    private String postDate;
    @SerializedName("post_datetime")
    private String postDatetime;
    @SerializedName("post_likes")
    private String postLikes;
    @SerializedName("post_image_status")
    private String postImageStatus;
    @SerializedName("post_share")
    private String postShare;
    @SerializedName("share_user_name")
    private String shareUserName;
    @SerializedName("log_name")
    private String logName;
    @SerializedName("log_pics")
    private String logPics;
    @SerializedName("likes_count")
    private String likesCount;
    @SerializedName("is_liked")
    private Boolean isLiked;
    @SerializedName("total_comments")
    private String totalComments;
    @SerializedName("comments_status")
    private Boolean commentsStatus;
    @SerializedName("comments_result")
    private List<SingleCommentResultPOJO> singleCommentResultPOJOList;
    @SerializedName("share_count")
    private String shareCount;
    @SerializedName("is_shared")
    private Boolean isShared;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }

    public String getPostCatId() {
        return postCatId;
    }

    public void setPostCatId(String postCatId) {
        this.postCatId = postCatId;
    }

    public String getPostMsg() {
        return postMsg;
    }

    public void setPostMsg(String postMsg) {
        this.postMsg = postMsg;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDatetime() {
        return postDatetime;
    }

    public void setPostDatetime(String postDatetime) {
        this.postDatetime = postDatetime;
    }

    public String getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(String postLikes) {
        this.postLikes = postLikes;
    }

    public String getPostImageStatus() {
        return postImageStatus;
    }

    public void setPostImageStatus(String postImageStatus) {
        this.postImageStatus = postImageStatus;
    }

    public String getPostShare() {
        return postShare;
    }

    public void setPostShare(String postShare) {
        this.postShare = postShare;
    }

    public String getShareUserName() {
        return shareUserName;
    }

    public void setShareUserName(String shareUserName) {
        this.shareUserName = shareUserName;
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

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public String getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(String totalComments) {
        this.totalComments = totalComments;
    }

    public Boolean getCommentsStatus() {
        return commentsStatus;
    }

    public void setCommentsStatus(Boolean commentsStatus) {
        this.commentsStatus = commentsStatus;
    }

    public List<SingleCommentResultPOJO> getSingleCommentResultPOJOList() {
        return singleCommentResultPOJOList;
    }

    public void setSingleCommentResultPOJOList(List<SingleCommentResultPOJO> singleCommentResultPOJOList) {
        this.singleCommentResultPOJOList = singleCommentResultPOJOList;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public Boolean getShared() {
        return isShared;
    }

    public void setShared(Boolean shared) {
        isShared = shared;
    }

    @Override
    public String toString() {
        return "SingleNewsFeedResultPOJO{" +
                "postId='" + postId + '\'' +
                ", postUserId='" + postUserId + '\'' +
                ", postCatId='" + postCatId + '\'' +
                ", postMsg='" + postMsg + '\'' +
                ", postImage='" + postImage + '\'' +
                ", postTime='" + postTime + '\'' +
                ", postDate='" + postDate + '\'' +
                ", postDatetime='" + postDatetime + '\'' +
                ", postLikes='" + postLikes + '\'' +
                ", postImageStatus='" + postImageStatus + '\'' +
                ", postShare='" + postShare + '\'' +
                ", shareUserName='" + shareUserName + '\'' +
                ", logName='" + logName + '\'' +
                ", logPics='" + logPics + '\'' +
                ", likesCount='" + likesCount + '\'' +
                ", isLiked=" + isLiked +
                ", totalComments='" + totalComments + '\'' +
                ", commentsStatus=" + commentsStatus +
                ", singleCommentResultPOJOList=" + singleCommentResultPOJOList +
                ", shareCount='" + shareCount + '\'' +
                ", isShared=" + isShared +
                '}';
    }
}
