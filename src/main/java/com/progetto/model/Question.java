package com.progetto.model;

import java.util.List;

public class Question {
    private String questionId;
    private String question;
    private List<String> options;
    private String correctAnswer;

    public Question() {
    }

    public Question(String questionId, String question, List<String> options, String correctAnswer) {
        this.questionId = questionId;
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    // Getters & Setters
    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
