package com.example.android.fistconnect;

import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Domantas on 2018-03-02.
 */
//Dull enemy class for the sake of testing game
public class Enemy implements Serializable {


    public ImageView imageView;
    public String username;
    public int level;
    public boolean isChallenged;

    public Enemy() {
        username = "";
        level = 0;
        isChallenged = true;
    }


}
