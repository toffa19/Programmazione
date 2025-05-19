// src/main/java/com/progetto/viewmodel/ExercisesViewModel.java
package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    private final List<MultipleChoiceExercise> exercises = new ArrayList<>();
    private int currentIndex = 0;
    private int wrongAnswersCount = 0;

    private Timeline timer;
    private int elapsedSeconds = 0;

    private final StringProperty questionText = new SimpleStringProperty();
    private final ObservableList<String> options = FXCollections.observableArrayList();
    private final StringProperty selectedAnswer = new SimpleStringProperty();
    private final StringProperty timerText = new SimpleStringProperty("00:00");

    private static String selectedTopic;
    private static String selectedQuestionId;

    public ExercisesViewModel() {
        this.exerciseRepository = new ExerciseRepository();
        this.userRepository = new UserRepository();
    }

    public static void setSelectedTopic(String topic) {
        selectedTopic = topic;
    }
    public static String getSelectedTopic() {
        return selectedTopic;
    }
    public static void setSelectedQuestionId(String questionId) {
        selectedQuestionId = questionId;
    }
    public static String getSelectedQuestionId() {
        return selectedQuestionId;
    }

    /**
     * Carica TUTTI gli esercizi per il topic selezionato: facile - medio - difficile
     */
    public void loadExercises() {
        if (selectedTopic == null) {
            throw new IllegalStateException("Devi settare prima il topic con setSelectedTopic()");
        }

        exercises.clear();
        for (MacroTopicEntry entry : exerciseRepository.getAllEntries()) {
            if (entry.getMacroTopic().equals(selectedTopic)) {
                exercises.addAll(entry.getLevels().getFacile());
                exercises.addAll(entry.getLevels().getMedio());
                exercises.addAll(entry.getLevels().getDifficile());
                break;
            }
        }
        wrongAnswersCount = 0;
        currentIndex = 0;
        if (!exercises.isEmpty()) {
            loadCurrentQuestion();
        } else {
            questionText.set("Nessun esercizio disponibile per: " + selectedTopic);
            options.clear();
        }
    }

    private void loadCurrentQuestion() {
        MultipleChoiceExercise ex = exercises.get(currentIndex);
        questionText.set(ex.getQuestion());
        options.setAll(ex.getOptions());
        selectedAnswer.set("");
        selectedQuestionId = ex.getQuestionId();
    }

    public void nextQuestion() {
        if (hasNext()) {
            currentIndex++;
            loadCurrentQuestion();
        }
    }

    public boolean hasNext() {
        return currentIndex < exercises.size() - 1;
    }

    /**
     * Valuta la risposta, incrementa wrongAnswersCount se sbagliata, salva l'utente e restituisce true se corretta
     */
    public boolean submitAnswer() {
        String answer = selectedAnswer.get();
        MultipleChoiceExercise ex = exercises.get(currentIndex);
        boolean correct = ex.getCorrectAnswer().equals(answer);
        if (!correct) {
            wrongAnswersCount++;
        }

        // Salva il risultato sul primo utente
        List<User> users = userRepository.getAllUsers();
        if (!users.isEmpty()) {
            User user = users.get(0);
            userRepository.updateUser(user);
        }
        return correct;
    }

    // --- getters per il controller ---
    public int getWrongAnswersCount() {
        return wrongAnswersCount;
    }

    public int getTotalQuestionsCount() {
        return exercises.size();
    }

    /**
     * Restituisce la risposta corretta dell'esercizio corrente
     */
    public String getCorrectAnswer() {
        if (exercises.isEmpty() || currentIndex < 0 || currentIndex >= exercises.size()) {
            return null;
        }
        return exercises.get(currentIndex).getCorrectAnswer();
    }

    // --- proprietà per il binding in UI ---
    public StringProperty questionTextProperty() {
        return questionText;
    }
    public ObservableList<String> getOptions() {
        return options;
    }
    public StringProperty selectedAnswerProperty() {
        return selectedAnswer;
    }
    public StringProperty timerTextProperty() {
        return timerText;
    }

    public void startTimer() {
        stopTimer();
        elapsedSeconds = 0;
        timerText.set("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
            elapsedSeconds++;
            int m = elapsedSeconds / 60;
            int s = elapsedSeconds % 60;
            timerText.set(String.format("%02d:%02d", m, s));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
}
