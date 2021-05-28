package com.example.cbsd_project.models;

public class User {

    public static String firebasePath = "users";
    public static String firebaseCountName = "user-count";

    private static User currentUser = null;

    private String userID;
    private String name;
    private String email;

    public User() {
    }

    public User(String name, String email, String userID) {
        this.setUserID(userID);
        this.setName(name);
        this.setEmail(email);
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        if(User.getCurrentUser() == null){
            User.currentUser = currentUser;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
