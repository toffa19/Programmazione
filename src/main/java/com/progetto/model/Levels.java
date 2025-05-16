package com.progetto.model;

// src/main/java/com/progetto/model/Levels.java

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds lists of exercises by difficulty: facile, medio, difficile.
 * Provides CRUD operations for exercises within each level.
 */
public class Levels {
    private List<MultipleChoiceExercise> facile = new ArrayList<>();
    private List<MultipleChoiceExercise> medio = new ArrayList<>();
    private List<MultipleChoiceExercise> difficile = new ArrayList<>();

    public List<MultipleChoiceExercise> getFacile() {
        return facile;
    }

    public void setFacile(List<MultipleChoiceExercise> facile) {
        this.facile = facile;
    }

    public List<MultipleChoiceExercise> getMedio() {
        return medio;
    }

    public void setMedio(List<MultipleChoiceExercise> medio) {
        this.medio = medio;
    }

    public List<MultipleChoiceExercise> getDifficile() {
        return difficile;
    }

    public void setDifficile(List<MultipleChoiceExercise> difficile) {
        this.difficile = difficile;
    }

    /**
     * Adds an exercise to the appropriate difficulty list.
     */
    public void addExercise(MultipleChoiceExercise exercise) {
        switch (exercise.getLevel()) {
            case "facile":
                facile.add(exercise);
                break;
            case "medio":
                medio.add(exercise);
                break;
            case "difficile":
                difficile.add(exercise);
                break;
            default:
                throw new IllegalArgumentException("Unknown level: " + exercise.getLevel());
        }
    }

    /**
     * Removes an exercise by its ID and difficulty.
     * @return true if removed, false otherwise
     */
    public boolean removeExercise(String questionId, String level) {
        return getListByLevel(level)
                .removeIf(ex -> ex.getQuestionId().equals(questionId));
    }

    /**
     * Replaces an existing exercise with the same ID.
     * @return true if replaced, false if not found.
     */
    public boolean replaceExercise(MultipleChoiceExercise updated) {
        List<MultipleChoiceExercise> list = getListByLevel(updated.getLevel());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getQuestionId().equals(updated.getQuestionId())) {
                list.set(i, updated);
                return true;
            }
        }
        return false;
    }

    private List<MultipleChoiceExercise> getListByLevel(String level) {
        switch (level) {
            case "facile":   return facile;
            case "medio":    return medio;
            case "difficile":return difficile;
            default: throw new IllegalArgumentException("Unknown level: " + level);
        }
    }
}
