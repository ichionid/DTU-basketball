package com.example.giannis.dtu_basketball;

/**
 * Created by giannis on 12/27/15.
 */
public class Player {
    private int number;
    private boolean playing;
    private long timeEnteredCourt;

    public Player(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public long getTimeEnteredCourt() {
        return timeEnteredCourt;
    }

    public void setTimeEnteredCourt(long timeEnteredCourt) {
        this.timeEnteredCourt = timeEnteredCourt;
    }

}
