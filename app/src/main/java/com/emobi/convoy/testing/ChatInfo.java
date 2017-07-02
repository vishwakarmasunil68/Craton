package com.emobi.convoy.testing;

/**
 * Created by sunil on 16-02-2017.
 */

public class ChatInfo {
    String sender_user_id;
    String sender_user_name;
    String reciever_user_id;
    String reciever_user_name;
    String chat_msg;
    String chat_file_url;

    public ChatInfo(String sender_user_id, String sender_user_name, String reciever_user_id, String reciever_user_name, String chat_msg, String chat_file_url) {
        this.sender_user_id = sender_user_id;
        this.sender_user_name = sender_user_name;
        this.reciever_user_id = reciever_user_id;
        this.reciever_user_name = reciever_user_name;
        this.chat_msg = chat_msg;
        this.chat_file_url = chat_file_url;
    }

    public ChatInfo() {

    }

    public String getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(String sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public String getSender_user_name() {
        return sender_user_name;
    }

    public void setSender_user_name(String sender_user_name) {
        this.sender_user_name = sender_user_name;
    }

    public String getReciever_user_id() {
        return reciever_user_id;
    }

    public void setReciever_user_id(String reciever_user_id) {
        this.reciever_user_id = reciever_user_id;
    }

    public String getReciever_user_name() {
        return reciever_user_name;
    }

    public void setReciever_user_name(String reciever_user_name) {
        this.reciever_user_name = reciever_user_name;
    }

    public String getChat_msg() {
        return chat_msg;
    }

    public void setChat_msg(String chat_msg) {
        this.chat_msg = chat_msg;
    }

    public String getChat_file_url() {
        return chat_file_url;
    }

    public void setChat_file_url(String chat_file_url) {
        this.chat_file_url = chat_file_url;
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "sender_user_id='" + sender_user_id + '\'' +
                ", sender_user_name='" + sender_user_name + '\'' +
                ", reciever_user_id='" + reciever_user_id + '\'' +
                ", reciever_user_name='" + reciever_user_name + '\'' +
                ", chat_msg='" + chat_msg + '\'' +
                ", chat_file_url='" + chat_file_url + '\'' +
                '}';
    }
}
