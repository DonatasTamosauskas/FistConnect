package com.example.android.fistconnect;

import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Domantas on 2018-03-03.
 */
public class CurrentUser extends Enemy{

    public CurrentUser() {    }

    public CurrentUser(String username){
        this.username = username;
        this.level = 0;
        this.isChallenged = false;
    }

    public CurrentUser(String username, int level){
        this.username = username;
        this.level = level;
        this.isChallenged = false;
    }

    public CurrentUser(String username, int level, int avatarID, String userID){
        this.username = username;
        this.level = level;
        this.isChallenged = false;
        this.avatarID = avatarID;
        this.userID =  userID;
    }


}
