// PathViewModel.java
package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashSet;
import java.util.Set;

public class PathViewModel {
    private final ExerciseRepository exerciseRepository;
    private final UserRepository     userRepository;
    private final ObservableList<MacroTopicModel> topics;

    public PathViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository     = new UserRepository();
        topics = FXCollections.observableArrayList();
    }

    /**
     * Carica tutti i macro-topic in ordine, imposta unlocked/passed dall'utente.
     */
    public void loadPathTopics() {
        topics.clear();
        var exList = exerciseRepository.getAllExercises();
        if (exList == null) return;

        Set<String> seen = new LinkedHashSet<>();
        for (MultipleChoiceExercise ex : exList) {
            seen.add(ex.getMacroTopic());
        }
        for (String title : seen) {
            String url = exList.stream()
                    .filter(e -> e.getMacroTopic().equals(title))
                    .findFirst()
                    // costruisco il path dall’attributo title
                    .map(e -> "/assets/images/path_icon_excercise.png")
                    // se non trovo nessun esercizio con quel macroTopic, uso l’icona di default
                    .orElse("/assets/images/path_icon_excercise.png");
            MacroTopicModel m = new MacroTopicModel(title, url);
            topics.add(m);
        }
        if (!topics.isEmpty()) topics.get(0).setUnlocked(true);

        String uid = SessionManager.getCurrentUserId();
        User user = userRepository.getAllUsers().stream()
                .filter(u -> uid.equals(u.getId()))
                .findFirst().orElse(null);
        if (user != null && user.getProgress() != null) {
            user.getProgress().forEach(p -> {
                topics.stream()
                        .filter(m -> m.getTitle().equalsIgnoreCase(p.getMacroTopic()))
                        .findFirst()
                        .ifPresent(m -> m.setPassed(p.isPassed()));
            });
        }
        for (int i = 0; i < topics.size() - 1; i++) {
            if (topics.get(i).isPassed()) {
                topics.get(i+1).setUnlocked(true);
            }
        }
    }

    public ObservableList<MacroTopicModel> getTopics() {
        return topics;
    }

    /**
     * Marca un topic come passed e sblocca il successivo.
     */
    public void markTopicPassed(String title) {
        for (int i = 0; i < topics.size(); i++) {
            MacroTopicModel m = topics.get(i);
            if (m.getTitle().equalsIgnoreCase(title)) {
                m.setPassed(true);
                if (i+1 < topics.size()) topics.get(i+1).setUnlocked(true);
                break;
            }
        }
    }
}