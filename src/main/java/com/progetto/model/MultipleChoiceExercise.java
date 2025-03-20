package com.progetto.model;

import java.util.List;
import java.util.Map;

public class MultipleChoiceExercise extends Exercise {

    public MultipleChoiceExercise() {
        super();
    }

    public MultipleChoiceExercise(String macroTopic, Map<String, List<Question>> levels) {
        super(macroTopic, levels);
    }

    @Override
    public boolean evaluateAnswer(String questionId, String answer) {
        // Scorre tutti i livelli per trovare la domanda
        for (List<Question> questions : getLevels().values()) {
            for (Question q : questions) {
                if (q.getQuestionId().equals(questionId)) {
                    // Confronta la risposta fornita (ignorando eventuali differenze di maiuscole/minuscole)
                    return q.getCorrectAnswer().equalsIgnoreCase(answer);
                }
            }
        }
        return false; // Se la domanda non viene trovata, o la risposta è errata
    }
}
