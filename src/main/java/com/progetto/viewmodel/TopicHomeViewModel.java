package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.model.Question;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Gestisce la "home" di un singolo topic.
 */
public class TopicHomeViewModel {

    private final ExerciseRepository exerciseRepository = new ExerciseRepository();
    private final UserRepository   userRepository     = new UserRepository();

    private String topicName;
    private String topicDescription;
    private final ObservableList<String> questionIds = FXCollections.observableArrayList();


    public TopicHomeViewModel() {
        // eventuale setup
    }

    /** Carica nome, descrizione e elenco di questionId per il topic selezionato */
    public void loadTopicDetails() {
        String selectedTopic = ExercisesViewModel.getSelectedTopic();
        List<MacroTopicEntry> entries = exerciseRepository.getAllEntries();

        for (MacroTopicEntry entry : entries) {
            if (entry.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                topicName = entry.getMacroTopic();
                topicDescription = "Breve descrizione di " + topicName; // placeholder

                // reset e popolamento questionIds
                questionIds.clear();
                // livello facile
                entry.getLevels().getFacile()
                        .forEach(q -> questionIds.add(q.getQuestionId()));
                // livello medio
                entry.getLevels().getMedio()
                        .forEach(q -> questionIds.add(q.getQuestionId()));
                // livello difficile
                entry.getLevels().getDifficile()
                        .forEach(q -> questionIds.add(q.getQuestionId()));

                break;
            }
        }
    }

    public String getTopicName() {
        return topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public ObservableList<String> getQuestionIds() {
        return questionIds;
    }

    /**
     * Controlla se una domanda è stata completata correttamente dall'utente corrente.
     */
    public boolean isQuestionCompleted(String questionId) {
        var allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) return false;
        var currentUser = allUsers.get(0); // sostituire con SessionManager.getCurrentUserId()

        if (currentUser.getProgress() != null) {
            for (var prog : currentUser.getProgress()) {
                if (prog.getQuestionResults() != null) {
                    for (var qr : prog.getQuestionResults()) {
                        if (qr.getQuestionId().equalsIgnoreCase(questionId) && qr.isCorrect()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void setSelectedQuestion(String questionId) {
        ExercisesViewModel.setSelectedQuestionId(questionId);
    }
}
