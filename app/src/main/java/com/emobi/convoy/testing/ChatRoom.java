package com.emobi.convoy.testing;

/**
 * Created by sunil on 16-02-2017.
 */

public class ChatRoom {
    String time;
    public ChatRoom(){

    }

    public ChatRoom( String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "time='" + time + '\'' +
                '}';
    }
}
