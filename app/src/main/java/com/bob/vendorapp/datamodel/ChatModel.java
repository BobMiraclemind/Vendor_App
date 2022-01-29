package com.bob.vendorapp.datamodel;

public class ChatModel {
    String message,uid,time;

    public ChatModel() {
    }

    public ChatModel(String message, String uid, String time) {
        this.message = message;
        this.uid = uid;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
