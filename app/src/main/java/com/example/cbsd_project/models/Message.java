package com.example.cbsd_project.models;

public class Message {

    public static String firebasePath = "messages";

    private String content;
    private String sender;
    private String messageType;
    private String messageViewType;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Message(String content, String sender, String messageType, String messageViewType) {
        this.setContent(content);
        this.setSender(sender);
        this.setMessageType(messageType);
        this.setMessageViewType(messageViewType);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageViewType() {
        return messageViewType;
    }

    public void setMessageViewType(String messageViewType) {
        this.messageViewType = messageViewType;
    }
}
