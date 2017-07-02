package com.emobi.convoy.pojo.userlike;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 08-05-2017.
 */

public class UserLikePOJO {
    @SerializedName("success")
    String succes;
    @SerializedName("result")
    List<LikeResultPOJO> userLikesResultPOJOList;

    public String getSucces() {
        return succes;
    }

    public void setSucces(String succes) {
        this.succes = succes;
    }

    public List<LikeResultPOJO> getUserLikesResultPOJOList() {
        return userLikesResultPOJOList;
    }

    public void setUserLikesResultPOJOList(List<LikeResultPOJO> userLikesResultPOJOList) {
        this.userLikesResultPOJOList = userLikesResultPOJOList;
    }

    @Override
    public String toString() {
        return "UserLikePOJO{" +
                "succes='" + succes + '\'' +
                ", userLikesResultPOJOList=" + userLikesResultPOJOList +
                '}';
    }
}
