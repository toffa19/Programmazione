package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.repository.ExerciseRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class HomeViewModel {

    private final ExerciseRepository exerciseRepository;
    private final ObservableList<MacroTopicModel> macroTopics;
    private final StringProperty precision = new SimpleStringProperty("Precisione: X / 100");

    public HomeViewModel() {
        exerciseRepository = new ExerciseRepository();
        macroTopics = FXCollections.observableArrayList();
    }

    public void loadMacroTopics() {
        // Usa getAllExercises() invece di getExercisesWrapper()
        var exercises = exerciseRepository.getAllExercises();
        if (exercises == null) {
            System.out.println("[LOG] Nessun esercizio trovato nel JSON.");
            return;
        }

        Set<String> usedTopics = new HashSet<>();

        for (MultipleChoiceExercise exercise : exercises) {
            String topic = exercise.getMacroTopic();
            if (!usedTopics.contains(topic)) {
                usedTopics.add(topic);
                String imageUrl = getImageForTopic(topic);
                MacroTopicModel model = new MacroTopicModel(topic, imageUrl);
                macroTopics.add(model);
            }
        }
    }

    public ObservableList<MacroTopicModel> getMacroTopics() {
        return macroTopics;
    }

    public StringProperty precisionProperty() {
        return precision;
    }

    private String getImageForTopic(String topic) {
        return "https://via.placeholder.com/80.png?text=" + topic;
    }
}
