package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.Progress;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.LinkedHashSet;

public class PathViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ObservableList<MacroTopicModel> topics;

    public PathViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository    = new UserRepository();
        topics            = FXCollections.observableArrayList();
    }

    /**
     * Carica i macro-topic in ordine di apparizione e li sblocca
     * solo se l’utente corrente ha passato (prog.isPassed()==true)
     * il topic precedente.
     */
    public void loadPathTopics() {
        topics.clear();

        var exercises = exerciseRepository.getAllExercises();
        if (exercises == null) return;

        // mantengo ordine di prima occorrenza
        Set<String> unique = new LinkedHashSet<>();
        for (var ex : exercises) {
            unique.add(ex.getMacroTopic());
        }
        List<String> titles = new ArrayList<>(unique);

        // recupera utente corrente
        String userId = SessionManager.getCurrentUserId();
        Optional<User> optU = userRepository.getAllUsers()
                .stream()
                .filter(u -> userId.equals(u.getId()))
                .findFirst();
        User currentUser = optU.orElse(null);

        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            String imageUrl = getImageForTopic(title);
            MacroTopicModel m = new MacroTopicModel(title, imageUrl);

            // sblocco il primo sempre
            if (i == 0) {
                m.setUnlocked(true);
            } else if (currentUser != null) {
                String prevTitle = titles.get(i - 1);
                Optional<Progress> prog = currentUser.getProgress() == null
                        ? Optional.empty()
                        : currentUser.getProgress()
                        .stream()
                        .filter(p -> p.getMacroTopic().equalsIgnoreCase(prevTitle))
                        .findFirst();
                // sblocco se passato
                m.setUnlocked(prog.isPresent() && prog.get().isPassed());
            }
            topics.add(m);
        }
    }

    public ObservableList<MacroTopicModel> getTopics() {
        return topics;
    }

    private String getImageForTopic(String topic) {
        return "https://via.placeholder.com/80.png?text=" + topic;
    }
}
