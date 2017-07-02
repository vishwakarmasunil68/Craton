package com.emobi.convoy.pojo.singlenewsfeed;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 08-05-2017.
 */

public class SingleNewsFeedPOJO {

    @SerializedName("success")
    String success;
    @SerializedName("result")
    SingleNewsFeedResultPOJO singleNewsFeedResultPOJO;

    @Override
    public String toString() {
        return "SingleNewsFeedPOJO{" +
                "success='" + success + '\'' +
                ", singleNewsFeedResultPOJO=" + singleNewsFeedResultPOJO +
                '}';
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public SingleNewsFeedResultPOJO getSingleNewsFeedResultPOJO() {
        return singleNewsFeedResultPOJO;
    }

    public void setSingleNewsFeedResultPOJO(SingleNewsFeedResultPOJO singleNewsFeedResultPOJO) {
        this.singleNewsFeedResultPOJO = singleNewsFeedResultPOJO;
    }
}
