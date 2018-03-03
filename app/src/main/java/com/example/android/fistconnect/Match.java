package com.example.android.fistconnect;

/**
 * Created by Asus on 3/3/2018.
 */

public class Match {
    Player player1;
    Player player2;
    String WinnerId;
    Boolean HasStarted=false;
    Boolean IsOver=false;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getWinnerId() {
        return WinnerId;
    }

    public Boolean getHasStarted() {
        return HasStarted;
    }

    public Boolean getOver() {
        return IsOver;
    }
}
