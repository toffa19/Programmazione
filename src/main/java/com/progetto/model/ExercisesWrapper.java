// src/main/java/com/progetto/model/ExercisesWrapper.java
package com.progetto.model;

import java.util.List;

/**
 * Wrapper per il JSON: una lista di MacroTopicEntry (ogni entry contiene il nome del topic e
 * i livelli con i loro esercizi).
 */
public class ExercisesWrapper {
    private List<MacroTopicEntry> exercises;

    public ExercisesWrapper() { }

    public ExercisesWrapper(List<MacroTopicEntry> exercises) {
        this.exercises = exercises;
    }

    public List<MacroTopicEntry> getExercises() {
        return exercises;
    }

    public void setExercises(List<MacroTopicEntry> exercises) {
        this.exercises = exercises;
    }
}
