package com.emobi.convoy.pojo.chat;

import com.emobi.convoy.pojo.chatpojo.ChatResultPOJO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 06-04-2017.
 */

public class ChatDateResultPOJO {
    @SerializedName("date")
    String date;
    @SerializedName("result")
    List<ChatResultPOJO> chatResultPOJOList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ChatResultPOJO> getChatResultPOJOList() {
        return chatResultPOJOList;
    }

    public void setChatResultPOJOList(List<ChatResultPOJO> chatResultPOJOList) {
        this.chatResultPOJOList = chatResultPOJOList;
    }

    @Override
    public String toString() {
        return "ChatDateResultPOJO{" +
                "date='" + date + '\'' +
                ", chatResultPOJOList=" + chatResultPOJOList +
                '}';
    }
}
