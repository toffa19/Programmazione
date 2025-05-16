// src/main/java/com/progetto/model/MultipleChoiceExercise.java
package com.progetto.model;

import java.util.List;

public class MultipleChoiceExercise extends Exercise {

    public MultipleChoiceExercise() {
        super();
    }

    public MultipleChoiceExercise(String macroTopic,
                                  String level,
                                  String questionId,
                                  String question,
                                  List<String> options,
                                  String correctAnswer) {
        super(macroTopic, level, questionId, question, options, correctAnswer);
    }

    @Override
    public boolean evaluateAnswer(String questionId, String answer) {
        // Se coincide l’ID della domanda, confronto la risposta
        if (!getQuestionId().equals(questionId)) return false;
        return getCorrectAnswer().equalsIgnoreCase(answer);
    }
}
