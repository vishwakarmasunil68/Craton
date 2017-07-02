package com.emobi.convoy.pojo.addvisitor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sunil on 12-05-2017.
 */

public class AddVisitorPOJO implements Serializable{
    @SerializedName("success")
    String success;
    @SerializedName("result")
    AddVisitorResultPOJO addVisitorResultPOJO;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public AddVisitorResultPOJO getAddVisitorResultPOJO() {
        return addVisitorResultPOJO;
    }

    public void setAddVisitorResultPOJO(AddVisitorResultPOJO addVisitorResultPOJO) {
        this.addVisitorResultPOJO = addVisitorResultPOJO;
    }

    @Override
    public String toString() {
        return "AddVisitorPOJO{" +
                "success='" + success + '\'' +
                ", addVisitorResultPOJO=" + addVisitorResultPOJO +
                '}';
    }
}
