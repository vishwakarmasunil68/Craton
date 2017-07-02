package com.emobi.convoy.pojo.friendrequest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 06-04-2017.
 */

public class FriendRequestPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<FrienRequestResultPOJO> frienRequestResultPOJOs;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<FrienRequestResultPOJO> getFrienRequestResultPOJOs() {
        return frienRequestResultPOJOs;
    }

    public void setFrienRequestResultPOJOs(List<FrienRequestResultPOJO> frienRequestResultPOJOs) {
        this.frienRequestResultPOJOs = frienRequestResultPOJOs;
    }

    @Override
    public String toString() {
        return "FriendRequestPOJO{" +
                "success='" + success + '\'' +
                ", frienRequestResultPOJOs=" + frienRequestResultPOJOs +
                '}';
    }
}
