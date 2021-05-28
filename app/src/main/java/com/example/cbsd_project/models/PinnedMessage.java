package com.example.cbsd_project.models;

public class PinnedMessage {

    public static String firebasePath = "pinned-messages";
    public static String firebaseCountName = "pinned-message-count";

    private String messageID;

    public PinnedMessage() {

    }

    public PinnedMessage(String messageID) {
        this.setMessageID(messageID);
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
