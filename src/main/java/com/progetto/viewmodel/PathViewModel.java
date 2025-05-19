// src/main/java/com/progetto/viewmodel/PathViewModel.java
package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathViewModel {
    private final ExerciseRepository exerciseRepository = new ExerciseRepository();
    private final UserRepository userRepository         = new UserRepository();
    private final ObservableList<MacroTopicModel> topics = FXCollections.observableArrayList();

    /**
     * Carica in topics la sequenza di MacroTopicModel, con flags
     * "unlocked" e "passed" calcolati sullo stato dell'utente loggato.
     */
    public void loadPathTopics() {
        topics.clear();

        // 1) Flatten di tutti gli esercizi
        List<MultipleChoiceExercise> allExercises = exerciseRepository.getAllEntries().stream()
                .flatMap(entry -> Stream.of(
                                entry.getLevels().getFacile().stream(),
                                entry.getLevels().getMedio().stream(),
                                entry.getLevels().getDifficile().stream()
                        ).flatMap(s -> s)
                )
                .collect(Collectors.toList());

        // 2) Raccogli titoli unici in ordine
        Set<String> seenTitles = new LinkedHashSet<>();
        for (MultipleChoiceExercise ex : allExercises) {
            seenTitles.add(ex.getMacroTopic());
        }

        // 3) Crea i modelli base
        for (String title : seenTitles) {
            // Supponiamo MacroTopicModel(String title, String iconPath)
            topics.add(new MacroTopicModel(title, "/assets/images/path_icon_exercise.png"));
        }

        if (topics.isEmpty()) {
            return;
        }

        // 4) Sblocca sempre il primo
        topics.get(0).setUnlocked(true);

        // 5) Recupera utente loggato
        String currentUserId = SessionManager.getCurrentUserId();
        Optional<User> maybeUser = userRepository.getAllUsers().stream()
                .filter(u -> currentUserId != null && currentUserId.equals(u.getId()))
                .findFirst();

        if (maybeUser.isEmpty()) {
            return;
        }
        User user = maybeUser.get();

        // 6) Marca passed/unlock successivo
        for (int i = 0; i < topics.size(); i++) {
            MacroTopicModel mt = topics.get(i);
            boolean passed = user.getProgress().stream()
                    .anyMatch(p -> p.getMacroTopic().equalsIgnoreCase(mt.getTitle()) && p.isPassed());
            if (passed) {
                mt.setPassed(true);
                if (i + 1 < topics.size()) {
                    topics.get(i + 1).setUnlocked(true);
                }
            }
        }
    }

    /** Restituisce l’ObservableList su cui si basa la View. */
    public ObservableList<MacroTopicModel> getTopics() {
        return topics;
    }

    /**
     * Segna in memoria (e persiste) che il topic specificato è stato completato,
     * poi ricarica tutti i flag passed/unlocked.
     */
    public void markTopicPassed(String title) {
        String currentUserId = SessionManager.getCurrentUserId();
        Optional<User> maybeUser = userRepository.getAllUsers().stream()
                .filter(u -> currentUserId != null && currentUserId.equals(u.getId()))
                .findFirst();

        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            user.getProgress().stream()
                    .filter(p -> p.getMacroTopic().equalsIgnoreCase(title))
                    .findFirst()
                    .ifPresent(p -> {
                        p.setPassed(true);
                        userRepository.updateUser(user);
                    });
        }
        // ricarica lo stato
        loadPathTopics();
    }
}
