package com.example.chat;

public class Message {
    public String senderId;
    public String text;
    public long timestamp;
    
    public Message() {}

    public Message(String senderId, String text) {
        this.senderId = senderId;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }
}
