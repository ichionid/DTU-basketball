package com.example.giannis.dtu_basketball;

import android.util.Log;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giannis on 12/29/15.
 */
public class ActiveSquad implements Serializable {
    private Map<Integer,Player> players = new HashMap<Integer,Player>();
    private Long startTime, stopTime;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        Log.i("sTART", Long.toString(startTime));
        this.startTime = startTime;
    }

    public Long getStopTime() {
        return stopTime;
    }

    public void setStopTime(Long stopTime) {
        this.stopTime = stopTime;
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        Log.i("Add player", Integer.toString(player.getNumber()));
        this.players.put(player.getNumber(), player);
    }
    public void removePlayer(int playerId) {
        Log.i("Remove player", Integer.toString(playerId));
        this.players.remove(playerId);
    }
    public void cloneObject(ActiveSquad asSource) {
        this.setStartTime(asSource.getStartTime());
        this.setStopTime(asSource.getStopTime());
        Map<Integer, Player> ps = asSource.getPlayers();
        for (Map.Entry<Integer, Player> entry : ps.entrySet()) {
            this.addPlayer(entry.getValue());
        }
    }
}
