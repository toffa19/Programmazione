package com.progetto.model;

public class QuestionResult {
    private String questionId;
    private int attempts;
    private boolean correct;

    public QuestionResult() {
    }

    public QuestionResult(String questionId, int attempts, boolean correct) {
        this.questionId = questionId;
        this.attempts = attempts;
        this.correct = correct;
    }

    // Getters & Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }

    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
