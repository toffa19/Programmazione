package com.progetto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TopicProgress {
    private int attempts = 0;
    private int wrongAttempts = 0;
    private boolean passed = false;
    private int bestTimeSeconds = Integer.MAX_VALUE;

    public TopicProgress() {
        // Jackson
    }

    @JsonProperty("attempts")
    public int getAttempts() {
        return attempts;
    }

    @JsonProperty("wrongAttempts")
    public int getWrongAttempts() {
        return wrongAttempts;
    }

    @JsonProperty("passed")
    public boolean isPassed() {
        return passed;
    }

    @JsonProperty("bestTimeSeconds")
    public int getBestTimeSeconds() {
        return bestTimeSeconds == Integer.MAX_VALUE ? 0 : bestTimeSeconds;
    }

    public void registerAttempt(boolean correct, int timeTaken) {
        attempts++;
        if (!correct) {
            wrongAttempts++;
        } else {
            if (!passed) passed = true;
            if (timeTaken < bestTimeSeconds) {
                bestTimeSeconds = timeTaken;
            }
        }
    }
}
