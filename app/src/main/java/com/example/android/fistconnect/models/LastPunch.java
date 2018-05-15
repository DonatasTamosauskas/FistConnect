package com.example.android.fistconnect.models;

import java.io.Serializable;

/**
 * Created by Domantas on 2018-03-04.
 */

public class LastPunch implements Serializable{
    public String userID;
    public int lastPunch;

    public LastPunch(String uId, int lastPunch) {
        userID = uId;
        this.lastPunch = lastPunch;
    }

    public LastPunch(){}
}
