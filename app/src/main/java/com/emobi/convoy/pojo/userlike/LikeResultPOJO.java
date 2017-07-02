package com.emobi.convoy.pojo.userlike;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 08-05-2017.
 */

public class LikeResultPOJO {
    @SerializedName("log_name")
    String log_name;
    @SerializedName("log_pics")
    String log_pics;

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
        return "LikeResultPOJO{" +
                "log_name='" + log_name + '\'' +
                ", log_pics='" + log_pics + '\'' +
                '}';
    }
}
