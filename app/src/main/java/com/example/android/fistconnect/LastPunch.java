package com.example.android.fistconnect;

/**
 * Created by Domantas on 2018-03-04.
 */

public class LastPunch {
    String userID;
    int lastPunch;

    LastPunch(Player player, int lastPunch) {
        userID = player.getUserId();
        this.lastPunch = lastPunch;
    }

    LastPunch(){};
}
