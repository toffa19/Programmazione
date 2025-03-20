package com.progetto.model;

import java.util.List;

public class Progress {
    private String macroTopic;
    private String difficulty; // "facile", "medio", "difficile"
    private List<QuestionResult> questionResults;
    private int score;

    public Progress() {
    }

    public Progress(String macroTopic, String difficulty, List<QuestionResult> questionResults, int score) {
        this.macroTopic = macroTopic;
        this.difficulty = difficulty;
        this.questionResults = questionResults;
        this.score = score;
    }

    // Getters & Setters
    public String getMacroTopic() { return macroTopic; }
    public void setMacroTopic(String macroTopic) { this.macroTopic = macroTopic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public List<QuestionResult> getQuestionResults() { return questionResults; }
    public void setQuestionResults(List<QuestionResult> questionResults) { this.questionResults = questionResults; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
