package com.example.cbsd_project.models;

public class Participant {

    public static String firebasePath = "participants";
    public static String firebaseCountName = "participant-count";

    private String nickname;
    private String userID;
    private String participantType;

    public Participant() {

    }

    public Participant(String nickname, String userID, String participantType){
        this.setNickname(nickname);
        this.setUserID(userID);
        this.setParticipantType(participantType);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getParticipantType() {
        return participantType;
    }

    public void setParticipantType(String participantType) {
        this.participantType = participantType;
    }
}
