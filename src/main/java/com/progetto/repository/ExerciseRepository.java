package com.progetto.repository;

import com.progetto.model.ExercisesWrapper;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.util.Config;

import java.util.ArrayList;

public class ExerciseRepository extends AbstractRepository<ExercisesWrapper> {

    private ExercisesWrapper exercisesWrapper;

    public ExerciseRepository() {
        super();
        loadExercises();
    }

    @Override
    protected String getFilePath() {
        return Config.EXERCISES_JSON_PATH;
    }

    private void loadExercises() {
        ExercisesWrapper loaded = loadData(ExercisesWrapper.class);
        if (loaded != null) {
            exercisesWrapper = loaded;
        } else {
            exercisesWrapper = new ExercisesWrapper(new ArrayList<>());
            saveExercises();
        }
    }

    public ExercisesWrapper getExercisesWrapper() {
        return exercisesWrapper;
    }

    public void addExercise(MultipleChoiceExercise exercise) {
        exercisesWrapper.getExercises().add(exercise);
        saveExercises();
    }

    private void saveExercises() {
        saveData(exercisesWrapper);
    }
}
