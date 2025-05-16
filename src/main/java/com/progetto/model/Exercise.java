// src/main/java/com/progetto/model/Exercise.java
package com.progetto.model;

import java.util.List;

public abstract class Exercise {
    private String macroTopic;
    private String level;
    private String questionId;
    private String question;
    private List<String> options;
    private String correctAnswer;

    public Exercise() {}

    public Exercise(String macroTopic, String level, String questionId,
                    String question, List<String> options, String correctAnswer) {
        this.macroTopic    = macroTopic;
        this.level         = level;
        this.questionId    = questionId;
        this.question      = question;
        this.options       = options;
        this.correctAnswer = correctAnswer;
    }

    public String getMacroTopic()      { return macroTopic; }
    public void   setMacroTopic(String t) { this.macroTopic = t; }

    public String getLevel()           { return level; }
    public void   setLevel(String l)      { this.level = l; }

    public String getQuestionId()      { return questionId; }
    public void   setQuestionId(String id) { this.questionId = id; }

    public String getQuestion()        { return question; }
    public void   setQuestion(String q)   { this.question = q; }

    public List<String> getOptions()   { return options; }
    public void         setOptions(List<String> opts) { this.options = opts; }

    public String getCorrectAnswer()   { return correctAnswer; }
    public void   setCorrectAnswer(String a) { this.correctAnswer = a; }

    /**
     * Ritorna true se l’answer passata coincide (case‐insensitive)
     * con la risposta corretta della domanda specificata.
     */
    public abstract boolean evaluateAnswer(String questionId, String answer);
}
