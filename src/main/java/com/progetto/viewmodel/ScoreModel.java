package com.progetto.viewmodel;

public class ScoreModel {
    private final String username;
    private final int points;

    public ScoreModel(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() { return username; }
    public int getPoints()   { return points;   }
}
