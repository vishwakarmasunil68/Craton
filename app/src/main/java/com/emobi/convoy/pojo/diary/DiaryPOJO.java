package com.emobi.convoy.pojo.diary;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 05-04-2017.
 */

public class DiaryPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<DiaryResultPOJO> diaryResultPOJOs;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DiaryResultPOJO> getDiaryResultPOJOs() {
        return diaryResultPOJOs;
    }

    public void setDiaryResultPOJOs(List<DiaryResultPOJO> diaryResultPOJOs) {
        this.diaryResultPOJOs = diaryResultPOJOs;
    }

    @Override
    public String toString() {
        return "DiaryPOJO{" +
                "success='" + success + '\'' +
                ", diaryResultPOJOs=" + diaryResultPOJOs +
                '}';
    }
}
