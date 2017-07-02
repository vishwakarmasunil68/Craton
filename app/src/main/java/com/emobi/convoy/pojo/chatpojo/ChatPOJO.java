package com.emobi.convoy.pojo.chatpojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 04-03-2017.
 */

public class ChatPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<ChatResultPOJO> list_chat;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ChatResultPOJO> getList_chat() {
        return list_chat;
    }

    public void setList_chat(List<ChatResultPOJO> list_chat) {
        this.list_chat = list_chat;
    }

    @Override
    public String toString() {
        return "ChatPOJO{" +
                "success='" + success + '\'' +
                ", list_chat=" + list_chat +
                '}';
    }
}
