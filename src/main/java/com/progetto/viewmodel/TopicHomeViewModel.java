package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.QuestionResult;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la "home" di un singolo topic.
 */
public class TopicHomeViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    private String topicName;         // es. "OOP"
    private String topicDescription;  // se presente nel JSON o definito altrove
    private final ObservableList<String> questionIds; // elenca le questionId (es. "OOP_F1", "OOP_F2"...)

    // Per semplicità, usiamo lo stesso metodo statico di ExercisesViewModel per sapere il topic selezionato
    public TopicHomeViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository = new UserRepository();
        questionIds = FXCollections.observableArrayList();
    }

    public void loadTopicDetails() {
        String selectedTopic = ExercisesViewModel.getSelectedTopic();
        // Oppure crea un TopicHomeViewModel.setSelectedTopic(...) se preferisci tenerli separati

        // Cerchiamo l'esercizio corrispondente
        var exercises = exerciseRepository.getAllExercises();
        if (exercises != null) {
            for (MultipleChoiceExercise ex : exercises) {
                if (ex.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                    // topicName
                    topicName = ex.getMacroTopic();
                    // Per la descrizione, potresti aggiungerla al JSON o usare un campo personalizzato
                    topicDescription = "Breve descrizione di " + ex.getMacroTopic(); // placeholder

                    // Aggiungiamo le questionId del livello "facile", "medio", "difficile"...
                    // Oppure solo di un livello
                    ex.getLevels().forEach((level, questions) -> {
                        questions.forEach(q -> questionIds.add(q.getQuestionId()));
                    });
                    break;
                }
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
        // Carichiamo l'utente corrente (dipende da come gestisci il login)
        // In un'app reale potresti avere un "SessionManager" con l'utente loggato
        // Qui semplifichiamo, cercando un utente "corrente" a scopo dimostrativo
        var allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) return false;
        var currentUser = allUsers.get(0); // placeholder: prendi il primo utente, o quello loggato

        // Verifichiamo se in currentUser.progress c'è un questionResult con questionId e correct = true
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

    /**
     * Se vuoi memorizzare la domanda selezionata (es. "OOP_F1") in un ViewModel statico
     * per poi navigare a ExercisesView, puoi farlo qui:
     */
    public void setSelectedQuestion(String questionId) {
        // Esempio: ExercisesViewModel.setSelectedQuestionId(questionId);
        // Oppure gestisci la logica di navigazione in un'altra classe
    }
}
