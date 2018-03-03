package com.example.android.fistconnect;

/**
 * Created by Asus on 3/3/2018.
 */

public class Player {
    String UserId;
    int PunchCount=0;
    int DamageDealt=0;
    int Health=100;
    String Username;
    int avatarID;

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setPunchCount(int punchCount) {
        PunchCount = punchCount;
    }

    public void setDamageDealt(int damageDealt) {
        DamageDealt = damageDealt;
    }

    public void setHealth(int health) {
        Health = health;
    }

    public String getUserId() {
        return UserId;
    }

    public int getPunchCount() {
        return PunchCount;
    }

    public int getDamageDealt() {
        return DamageDealt;
    }

    public int getHealth() {
        return Health;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
