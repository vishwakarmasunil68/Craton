package com.emobi.convoy.pojo.chatpojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 04-03-2017.
 */

public class ChatResultPOJO {

    @SerializedName("chat_id")
    String chat_id;
    @SerializedName("chat_fri_id")
    String chat_fri_id;
    @SerializedName("chat_user_id")
    String chat_user_id;
    @SerializedName("date")
    String date;
    @SerializedName("time")
    String time;
    @SerializedName("chat_msg")
    String chat_msg;
    @SerializedName("chat_title")
    String chat_title;
    @SerializedName("chat_file")
    String chat_file;
    @SerializedName("file_type")
    String file_type;

    public ChatResultPOJO(String chat_id, String chat_fri_id, String chat_user_id, String date, String time, String chat_msg, String chat_title, String chat_file, String file_type) {
        this.chat_id = chat_id;
        this.chat_fri_id = chat_fri_id;
        this.chat_user_id = chat_user_id;
        this.date = date;
        this.time = time;
        this.chat_msg = chat_msg;
        this.chat_title = chat_title;
        this.chat_file = chat_file;
        this.file_type = file_type;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_fri_id() {
        return chat_fri_id;
    }

    public void setChat_fri_id(String chat_fri_id) {
        this.chat_fri_id = chat_fri_id;
    }

    public String getChat_user_id() {
        return chat_user_id;
    }

    public void setChat_user_id(String chat_user_id) {
        this.chat_user_id = chat_user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChat_msg() {
        return chat_msg;
    }

    public void setChat_msg(String chat_msg) {
        this.chat_msg = chat_msg;
    }

    public String getChat_title() {
        return chat_title;
    }

    public void setChat_title(String chat_title) {
        this.chat_title = chat_title;
    }

    public String getChat_file() {
        return chat_file;
    }

    public void setChat_file(String chat_file) {
        this.chat_file = chat_file;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    @Override
    public String toString() {
        return "ChatResultPOJO{" +
                "chat_id='" + chat_id + '\'' +
                ", chat_fri_id='" + chat_fri_id + '\'' +
                ", chat_user_id='" + chat_user_id + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", chat_msg='" + chat_msg + '\'' +
                ", chat_title='" + chat_title + '\'' +
                ", chat_file='" + chat_file + '\'' +
                ", file_type='" + file_type + '\'' +
                '}';
    }
}
