package com.example.android.fistconnect.models;

import com.example.android.fistconnect.models.LastPunch;
import com.example.android.fistconnect.models.Player;

import java.io.Serializable;

/**
 * Created by Asus on 3/3/2018.
 */


public class Match implements Serializable {

    private Player player1;
    private Player player2;
    private String winnerId;
    private Boolean hasStarted = false;
    private Boolean isOver = false;
    public boolean hasPunched = false;
    public LastPunch lastPunch;

    public Match() {
        player1 = new Player();
        player2 = new Player();
        lastPunch = new LastPunch();
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
