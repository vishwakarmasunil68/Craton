package com.emobi.convoy.pojo.diary;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 05-04-2017.
 */

public class AddDiaryPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    DiaryResultPOJO diaryResultPOJO;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public DiaryResultPOJO getDiaryResultPOJO() {
        return diaryResultPOJO;
    }

    public void setDiaryResultPOJO(DiaryResultPOJO diaryResultPOJO) {
        this.diaryResultPOJO = diaryResultPOJO;
    }

    @Override
    public String toString() {
        return "AddDiaryPOJO{" +
                "success='" + success + '\'' +
                ", diaryResultPOJO=" + diaryResultPOJO +
                '}';
    }
}
