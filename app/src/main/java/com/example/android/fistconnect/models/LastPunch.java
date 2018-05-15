package com.example.android.fistconnect.models;

import java.io.Serializable;

/**
 * Created by Domantas on 2018-03-04.
 */

public class LastPunch implements Serializable{
    private String userID;
    private MoveBy userRole;

    public LastPunch(String uId, MoveBy userRole) {
        userID = uId;
        this.userRole = userRole;
    }

    public LastPunch(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public MoveBy getUserRole() {
        return userRole;
    }

    public void setUserRole(MoveBy userRole) {
        this.userRole = userRole;
    }
}
