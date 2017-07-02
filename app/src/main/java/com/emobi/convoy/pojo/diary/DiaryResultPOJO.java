package com.emobi.convoy.pojo.diary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 05-04-2017.
 */

public class DiaryResultPOJO {
    @SerializedName("diary_id")
    String diary_id;
    @SerializedName("diary_msg")
    String diary_msg;
    @SerializedName("diary_datetime")
    String diary_datetime;
    @SerializedName("diary_user_id")
    String diary_user_id;

    public String getDiary_id() {
        return diary_id;
    }

    public void setDiary_id(String diary_id) {
        this.diary_id = diary_id;
    }

    public String getDiary_msg() {
        return diary_msg;
    }

    public void setDiary_msg(String diary_msg) {
        this.diary_msg = diary_msg;
    }

    public String getDiary_datetime() {
        return diary_datetime;
    }

    public void setDiary_datetime(String diary_datetime) {
        this.diary_datetime = diary_datetime;
    }

    public String getDiary_user_id() {
        return diary_user_id;
    }

    public void setDiary_user_id(String diary_user_id) {
        this.diary_user_id = diary_user_id;
    }

    @Override
    public String toString() {
        return "DiaryResultPOJO{" +
                "diary_id='" + diary_id + '\'' +
                ", diary_msg='" + diary_msg + '\'' +
                ", diary_datetime='" + diary_datetime + '\'' +
                ", diary_user_id='" + diary_user_id + '\'' +
                '}';
    }
}
