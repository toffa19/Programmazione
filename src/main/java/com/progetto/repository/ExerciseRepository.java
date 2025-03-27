package com.progetto.repository;

import com.progetto.model.ExercisesWrapper;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.util.Config;

import java.util.ArrayList;
import java.util.List;

public class ExerciseRepository extends AbstractRepository<ExercisesWrapper> {

    private ExercisesWrapper exercisesWrapper;

    public ExerciseRepository() {
        super();
        loadExercises();
    }

    @Override
    protected String getFilePath() {
        // Utilizza il percorso centralizzato definito in Config
        return Config.EXERCISES_JSON_PATH;
    }

    private void loadExercises() {
        ExercisesWrapper loaded = loadData(ExercisesWrapper.class);
        if (loaded != null) {
            exercisesWrapper = loaded;
            System.out.println("[LOG] Loaded " + exercisesWrapper.getExercises().size() + " exercise(s) from JSON.");
        } else {
            System.out.println("[LOG] No data found. Initializing new ExercisesWrapper.");
            exercisesWrapper = new ExercisesWrapper(new ArrayList<>());
            saveExercises();
        }
    }

    /**
     * Restituisce la lista di tutti gli esercizi (MultipleChoiceExercise) caricati dal file JSON.
     */
    public List<MultipleChoiceExercise> getAllExercises() {
        return exercisesWrapper.getExercises();
    }

    /**
     * Aggiunge un nuovo esercizio e salva su file JSON.
     */
    public void addExercise(MultipleChoiceExercise exercise) {
        exercisesWrapper.getExercises().add(exercise);
        System.out.println("[LOG] Added new exercise for macroTopic: " + exercise.getMacroTopic());
        saveExercises();
    }

    /**
     * Aggiorna un esercizio esistente in base al macroTopic (o altra logica a tua scelta).
     */
    public void updateExercise(MultipleChoiceExercise updatedExercise) {
        List<MultipleChoiceExercise> list = exercisesWrapper.getExercises();
        boolean updated = false;

        for (int i = 0; i < list.size(); i++) {
            // Qui la logica di "uguaglianza" la basiamo su macroTopic.
            // Puoi cambiarla se hai un id univoco.
            if (list.get(i).getMacroTopic().equals(updatedExercise.getMacroTopic())) {
                list.set(i, updatedExercise);
                updated = true;
                System.out.println("[LOG] Updated exercise for macroTopic: " + updatedExercise.getMacroTopic());
                break;
            }
        }

        if (!updated) {
            System.out.println("[LOG] Exercise with macroTopic '" + updatedExercise.getMacroTopic() + "' not found.");
        }
        saveExercises();
    }

    private void saveExercises() {
        saveData(exercisesWrapper);
    }
}
