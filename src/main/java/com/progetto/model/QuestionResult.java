package com.progetto.model;

public class QuestionResult {
    private String questionId;
    private int attempts;
    private String time; // formato "HH:mm:ss" oppure secondi come stringa
    private boolean correct;

    public QuestionResult() {
    }

    public QuestionResult(String questionId, int attempts, String time, boolean correct) {
        this.questionId = questionId;
        this.attempts = attempts;
        this.time = time;
        this.correct = correct;
    }

    // Getters & Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
