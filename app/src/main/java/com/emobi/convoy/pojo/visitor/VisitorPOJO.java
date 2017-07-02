package com.emobi.convoy.pojo.visitor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sunil on 06-04-2017.
 */

public class VisitorPOJO implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<VisitorResultPOJO> visitorResultPOJOList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<VisitorResultPOJO> getVisitorResultPOJOList() {
        return visitorResultPOJOList;
    }

    public void setVisitorResultPOJOList(List<VisitorResultPOJO> visitorResultPOJOList) {
        this.visitorResultPOJOList = visitorResultPOJOList;
    }

    @Override
    public String toString() {
        return "VisitorPOJO{" +
                "success='" + success + '\'' +
                ", visitorResultPOJOList=" + visitorResultPOJOList +
                '}';
    }
}
