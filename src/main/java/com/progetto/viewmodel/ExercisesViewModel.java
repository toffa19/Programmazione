package com.progetto.viewmodel;

import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.Question;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final List<Question> questions; // Lista di tutte le domande per il topic
    private static String selectedTopic;

    private int currentQuestionIndex = 0;
    private int consecutiveCorrect = 0;
    private final int thresholdForNextLevel = 3;
    private int errorCount = 0;

    private Timeline timer;
    private int elapsedSeconds = 0;

    public ExercisesViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository = new UserRepository();
        questions = new ArrayList<>();
    }

    public static void setSelectedTopic(String topic) {
        selectedTopic = topic;
    }

    public static String getSelectedTopic() {
        return selectedTopic;
    }

    /**
     * Carica tutte le domande per il topic selezionato (da tutti i livelli) dal JSON.
     */
    public void loadQuestionsForTopic() {
        questions.clear();
        var allExercises = exerciseRepository.getAllExercises();
        if (allExercises != null) {
            for (MultipleChoiceExercise exercise : allExercises) {
                if (exercise.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                    exercise.getLevels().forEach((level, qList) -> {
                        questions.addAll(qList);
                    });
                }
            }
        }
        currentQuestionIndex = 0;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public void moveToNextQuestion() {
        currentQuestionIndex++;
    }

    /**
     * Aggiorna il progresso dell'utente per la domanda specificata e salva nel JSON.
     */
    public void updateUserProgress(String questionId, boolean correct) {
        List<User> allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) {
            return;
        }
        // Per semplicità, usiamo il primo utente come quello loggato
        User currentUser = allUsers.get(0);

        if (currentUser.getProgress() == null) {
            currentUser.setProgress(new ArrayList<>());
        }
        boolean progressFound = false;
        for (var prog : currentUser.getProgress()) {
            if (prog.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                progressFound = true;
                if (prog.getQuestionResults() == null) {
                    prog.setQuestionResults(new ArrayList<>());
                }
                boolean found = false;
                for (var qr : prog.getQuestionResults()) {
                    if (qr.getQuestionId().equalsIgnoreCase(questionId)) {
                        qr.setAttempts(qr.getAttempts() + 1);
                        if (correct) {
                            qr.setCorrect(true);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    var newQR = new com.progetto.model.QuestionResult(questionId, 1, correct);
                    prog.getQuestionResults().add(newQR);
                }
                break;
            }
        }
        // Se non esiste un Progress per questo topic, crealo
        if (!progressFound) {
            com.progetto.model.Progress newProg = new com.progetto.model.Progress();
            newProg.setMacroTopic(selectedTopic);
            newProg.setScore(0);
            List<com.progetto.model.QuestionResult> newQRList = new ArrayList<>();
            newQRList.add(new com.progetto.model.QuestionResult(questionId, 1, correct));
            newProg.setQuestionResults(newQRList);
            // Inizialmente, non abbiamo ancora registrato il tempo
            newProg.setTime(null);
            // Il flag "passed" sarà impostato alla fine dell'esercizio
            newProg.setPassed(false);
            currentUser.getProgress().add(newProg);
        }
        userRepository.updateUser(currentUser);
    }

    /**
     * Aggiorna il tempo totale impiegato per il topic e segna il topic come superato se l'errore totale è inferiore a 3.
     * Se nel JSON è già presente un tempo, mantiene quello migliore (il minore).
     */
    public void updateTopicTime() {
        int timeTaken = elapsedSeconds;
        List<User> allUsers = userRepository.getAllUsers();
        if (allUsers.isEmpty()) return;
        User currentUser = allUsers.get(0);
        if (currentUser.getProgress() != null) {
            for (var prog : currentUser.getProgress()) {
                if (prog.getMacroTopic().equalsIgnoreCase(selectedTopic)) {
                    // Gestione del tempo: se il tempo esistente è vuoto o maggiore, aggiorna
                    String currentTimeStr = prog.getTime();
                    int currentTime = (currentTimeStr != null && !currentTimeStr.isEmpty())
                            ? Integer.parseInt(currentTimeStr)
                            : Integer.MAX_VALUE;
                    if (timeTaken < currentTime) {
                        prog.setTime(String.valueOf(timeTaken));
                    }
                    // Segna il topic come superato se sono stati fatti meno di 3 errori durante l'esercizio
                    prog.setPassed(errorCount < 3);
                    break;
                }
            }
        }
        userRepository.updateUser(currentUser);
    }

    public void processAnswerResult(boolean correct) {
        if (correct) {
            consecutiveCorrect++;
        } else {
            consecutiveCorrect = 0;
            errorCount++;
        }
        if (consecutiveCorrect >= thresholdForNextLevel) {
            System.out.println("Sbloccato il livello successivo per il topic: " + selectedTopic);
            // Logica per passare al livello successivo (da integrare ulteriormente)
        }
    }

    public int getErrorCount() {
        return errorCount;
    }

    // Timer e backup progressivo
    public void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            System.out.println("Tempo: " + elapsedSeconds + " secondi");
            if (elapsedSeconds % 30 == 0) {
                backupProgress();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void backupProgress() {
        System.out.println("Backup progressivo...");
        // Puoi chiamare userRepository.updateUser(...) per salvare lo stato corrente
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }
}