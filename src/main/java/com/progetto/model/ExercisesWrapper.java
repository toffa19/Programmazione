package com.progetto.model;

import java.util.List;

public class ExercisesWrapper {
    private List<MultipleChoiceExercise> exercises;

    public ExercisesWrapper() {
    }

    public ExercisesWrapper(List<MultipleChoiceExercise> exercises) {
        this.exercises = exercises;
    }

    public List<MultipleChoiceExercise> getExercises() { return exercises; }
    public void setExercises(List<MultipleChoiceExercise> exercises) { this.exercises = exercises; }
}
