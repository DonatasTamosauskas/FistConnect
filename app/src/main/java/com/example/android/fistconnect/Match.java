package com.example.android.fistconnect;

/**
 * Created by Asus on 3/3/2018.
 */

public class Match {
    Player player1;
    Player player2;
    String winnerId;
    Boolean hasStarted=false;
    Boolean isOver=false;

    public Match() {
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public Boolean getHasStarted() {
        return hasStarted;
    }

    public Boolean getIsOver() {
        return isOver;
    }

    public void setIsOver(Boolean over) {
        isOver = over;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;

    }
}
