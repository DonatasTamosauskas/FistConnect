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

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setWinnerId(String winnerId) {
        WinnerId = winnerId;
    }

    public void setHasStarted(Boolean hasStarted) {
        HasStarted = hasStarted;
    }

    public void setOver(Boolean over) {
        IsOver = over;
    }

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
