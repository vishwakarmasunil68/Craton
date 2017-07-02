package com.emobi.convoy.pojo.usercomment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 15-05-2017.
 */

public class UserCommentPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<UserCommentResultPOJO> userCommentResultPOJOList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<UserCommentResultPOJO> getUserCommentResultPOJOList() {
        return userCommentResultPOJOList;
    }

    public void setUserCommentResultPOJOList(List<UserCommentResultPOJO> userCommentResultPOJOList) {
        this.userCommentResultPOJOList = userCommentResultPOJOList;
    }

    @Override
    public String toString() {
        return "UserCommentPOJO{" +
                "success='" + success + '\'' +
                ", userCommentResultPOJOList=" + userCommentResultPOJOList +
                '}';
    }
}
