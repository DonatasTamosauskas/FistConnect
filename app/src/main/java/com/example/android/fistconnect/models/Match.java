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
    private Boolean over = false;
    private boolean hasPunched = false;
    private LastPunch lastPunch;

    public Match() {}

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

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public Boolean getOver() {
        return over;
    }

    public void setOver(Boolean over) {
        this.over = over;
    }

    public boolean isHasPunched() {
        return hasPunched;
    }

    public void setHasPunched(boolean hasPunched) {
        this.hasPunched = hasPunched;
    }

    public LastPunch getLastPunch() {
        return lastPunch;
    }

    public void setLastPunch(LastPunch lastPunch) {
        this.lastPunch = lastPunch;
    }
}
