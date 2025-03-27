package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.repository.ExerciseRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class PathViewModel {

    private final ExerciseRepository exerciseRepository;
    private final ObservableList<TopicModel> topics;

    public PathViewModel() {
        exerciseRepository = new ExerciseRepository();
        topics = FXCollections.observableArrayList();
    }

    /**
     * Carica i topic dal JSON degli esercizi.
     * Per ogni esercizio, estrae il macroTopic e crea un TopicModel solo se non è già presente.
     * Per semplicità, il lessonCount viene impostato come il numero di domande nel livello "facile"
     * e il primo topic viene sbloccato, mentre gli altri sono inizialmente bloccati.
     */
    public void loadPathTopics() {
        topics.clear();
        var allExercises = exerciseRepository.getAllExercises();
        if (allExercises != null) {
            Set<String> uniqueTopics = new HashSet<>();
            for (MultipleChoiceExercise ex : allExercises) {
                String macroTopic = ex.getMacroTopic();
                if (!uniqueTopics.contains(macroTopic)) {
                    uniqueTopics.add(macroTopic);
                    int lessonCount = 0;
                    if (ex.getLevels().containsKey("facile")) {
                        lessonCount = ex.getLevels().get("facile").size();
                    }
                    int practiceCount = 0; // Puoi aggiornare questa logica in base al numero di esercizi completati
                    // Il primo topic viene sbloccato, gli altri bloccati (puoi modificare la logica)
                    boolean unlocked = topics.isEmpty();
                    TopicModel topicModel = new TopicModel(macroTopic, "Descrizione di " + macroTopic, lessonCount, practiceCount, unlocked);
                    topics.add(topicModel);
                }
            }
        }
    }

    public ObservableList<TopicModel> getTopics() {
        return topics;
    }
}
