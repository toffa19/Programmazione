package com.progetto.viewmodel;

import javafx.beans.property.*;

public class ScoreModel {
    private final StringProperty userId     = new SimpleStringProperty();
    private final StringProperty username   = new SimpleStringProperty();
    private final IntegerProperty points    = new SimpleIntegerProperty();

    public ScoreModel(String userId, String username, int points) {
        this.userId.set(userId);
        this.username.set(username);
        this.points.set(points);
    }

    public String getUserId()     { return userId.get(); }
    public String getUsername()   { return username.get(); }
    public int    getPoints()     { return points.get(); }

    public StringProperty usernameProperty() { return username; }
    public IntegerProperty pointsProperty()  { return points; }
}
