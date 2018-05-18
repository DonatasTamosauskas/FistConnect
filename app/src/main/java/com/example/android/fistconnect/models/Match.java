package com.example.android.fistconnect.models;

import java.io.Serializable;

public class Match implements Serializable {

    private Player player1;
    private Player player2;
    private Boolean hasStarted;
    private Boolean over;
    private LastPunch lastPunch;

    public Match() {
        hasStarted = false;
        over = false;
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

    public LastPunch getLastPunch() {
        return lastPunch;
    }

    public void setLastPunch(LastPunch lastPunch) {
        this.lastPunch = lastPunch;
    }
}
