package com.example.android.fistconnect;

import java.io.Serializable;

/**
 * Created by Domantas on 2018-03-02.
 */
//Dull enemy class for the sake of testing game
public class Enemy implements Serializable {


    public int avatarID;
    public int level;
    public String username;
    public String userID;
    public boolean isChallenged;

    public Enemy() {
        username = "";
        level = 0;
        isChallenged = true;
    }


}
