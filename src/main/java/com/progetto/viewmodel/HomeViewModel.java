package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ObservableList<MacroTopicModel> macroTopics;
    private final StringProperty precision = new SimpleStringProperty("Precisione: X / 100");

    public HomeViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository = new UserRepository();
        macroTopics = FXCollections.observableArrayList();
    }

    public void loadMacroTopics() {
        macroTopics.clear();
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
        List<MacroTopicModel> topicsList = new ArrayList<>(macroTopics);
        String currentUserId = SessionManager.getCurrentUserId();
        User currentUser = null;
        for (User u : userRepository.getAllUsers()) {
            if (u.getId() != null && u.getId().equals(currentUserId)) {
                currentUser = u;
                break;
            }
        }
        if (currentUser == null) {
            currentUser = new User();
        }
        // Il primo topic è sbloccato per default
        for (int i = 0; i < topicsList.size(); i++) {
            MacroTopicModel topicModel = topicsList.get(i);
            if (i == 0) {
                topicModel.setUnlocked(true);
            } else {
                boolean previousPassed = false;
                String previousTopicTitle = topicsList.get(i - 1).getTitle();
                if (currentUser.getProgress() != null) {
                    for (var prog : currentUser.getProgress()) {
                        if (prog.getMacroTopic().equalsIgnoreCase(previousTopicTitle)) {
                            if (prog.isPassed()) {
                                previousPassed = true;
                                break;
                            }
                        }
                    }
                }
                topicModel.setUnlocked(previousPassed);
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
