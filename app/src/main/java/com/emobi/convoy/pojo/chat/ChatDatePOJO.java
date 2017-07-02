package com.emobi.convoy.pojo.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 06-04-2017.
 */

public class ChatDatePOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<ChatDateResultPOJO> chatDateResultPOJOs;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ChatDateResultPOJO> getChatDateResultPOJOs() {
        return chatDateResultPOJOs;
    }

    public void setChatDateResultPOJOs(List<ChatDateResultPOJO> chatDateResultPOJOs) {
        this.chatDateResultPOJOs = chatDateResultPOJOs;
    }

    @Override
    public String toString() {
        return "ChatDatePOJO{" +
                "success='" + success + '\'' +
                ", chatDateResultPOJOs=" + chatDateResultPOJOs +
                '}';
    }
}
