package com.example.android.fistconnect.models;

import java.io.Serializable;

public class User implements Serializable {

    private int avatarID;
    private int level;
    private String username;
    private String userID;

    public User() {
        username = "";
        level = 0;
    }

    public User(String username, int level, int avatarID, String userID){
        this.setUsername(username);
        this.setLevel(level);
        this.setAvatarID(avatarID);
        this.setUserID(userID);
    }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
