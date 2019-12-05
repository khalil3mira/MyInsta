package com.example.khalilaamira.myinsta.entities;

/**
 * Created by Khalil Aamira on 23/12/2017.
 */

public class Message {

    private String content;
    private String SenderID;
    private String timeSending;

    public Message() {
    }

    public Message(String content, String senderID, String timeSending) {
        this.content = content;
        SenderID = senderID;
        this.timeSending = timeSending;
    }

    public String getTimeSending() {
        return timeSending;
    }

    public void setTimeSending(String timeSending) {
        this.timeSending = timeSending;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        this.SenderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
