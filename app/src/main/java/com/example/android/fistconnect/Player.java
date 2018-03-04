package com.example.android.fistconnect;

import java.io.Serializable;

/**
 * Created by Asus on 3/3/2018.
 */

public class Player implements Serializable {
    String userId;
    int punchCount;
    int health;
    String username;

    public Player() {
        health = 3;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPunchCount() {
        return punchCount;
    }

    public void setPunchCount(int punchCount) {
        this.punchCount = punchCount;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
