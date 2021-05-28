package com.example.cbsd_project.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Room {

    public static String firebasePath = "rooms";

    private static Room currentRoom = null;

    private String roomID;
    private String name;
    private String roomType;

    public Room() {

    }

    public Room(String username, String roomType, String roomID) {
        this.setRoomID(roomID);
        this.setName(username);
        this.setRoomType(roomType);
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public static void setCurrentRoom(Room currentRoom) {
        Room.currentRoom = currentRoom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}