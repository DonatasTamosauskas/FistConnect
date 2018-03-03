package com.example.android.fistconnect;

import android.widget.ImageView;

/**
 * Created by Domantas on 2018-03-02.
 */
//Dull enemy class for the sake of testing game
public class Enemy {

    public ImageView imageView;
    public int avatarID;
    public int level;
    public String username;
    public String userID;
    public boolean isChallenged;

    public Enemy() {
        username = "Jonas";
        level = 0;
        isChallenged = true;
    }


}
