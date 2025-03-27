package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.Question;
import com.progetto.model.QuestionResult;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    private final ObservableList<String> questionIds; // ID delle domande (es. "OOP_F1")
    private final List<Question> questions;           // Oggetti Question
    private static String selectedTopic;

    private int currentQuestionIndex = 0;

    public ExercisesViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository = new UserRepository();
        questionIds = FXCollections.observableArrayList();
        questions = new ArrayList<>();
    }

    public static void setSelectedTopic(String topic) {
        selectedTopic = topic;
    }

    public static String getSelectedTopic() {
        return selectedTopic;
    }

    public ObservableList<String> getQuestionIds() {
        return questionIds;
    }

    public void setCurrentQuestionIndex(int index) {
        currentQuestionIndex = index;
    }
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void loadQuestionsForTopic() {
        questionIds.clear();
        questions.clear();
        var allExercises = exerciseRepository.getAllExercises();
        if (allExercises != null) {
            for (MultipleChoiceExercise exercise : allExercises) {
                if (exercise.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                    // Carichiamo (ad es.) tutti i livelli "facile", "medio", "difficile"
                    exercise.getLevels().forEach((level, qList) -> {
                        for (Question q : qList) {
                            questions.add(q);
                            questionIds.add(q.getQuestionId());
                        }
                    });
                }
            }
        }
        currentQuestionIndex = 0;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public void updateUserProgress(String questionId, boolean correct) {
        // Qui ipotizziamo un solo utente "corrente". In un'app reale avresti un session manager
        List<User> allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) {
            return;
        }
        User currentUser = allUsers.get(0); // Esempio: prendi il primo come "utente loggato"

        // Trova se esiste già un questionResult per questa questionId
        var found = false;
        if (currentUser.getProgress() != null) {
            for (var prog : currentUser.getProgress()) {
                // Se combacia il macroTopic (selectedTopic), e.g. "OOP"
                if (prog.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                    var qrList = prog.getQuestionResults();
                    for (var qr : qrList) {
                        if (qr.getQuestionId().equalsIgnoreCase(questionId)) {
                            qr.setCorrect(correct);
                            found = true;
                            break;
                        }
                    }
                    // Se non trovato, lo aggiungiamo
                    if (!found) {
                        var newQR = new QuestionResult(questionId, 1, "00:00:00", correct);
                        qrList.add(newQR);
                        found = true;
                    }
                }
            }
        }
        // Se non esiste un Progress per questo topic, crealo (semplificando la logica)
        // ... (codice per aggiungere un nuovo Progress se necessario)

        // Salviamo i cambiamenti
        userRepository.updateUser(currentUser);
    }
}
