package com.progetto.viewmodel;

import com.progetto.model.MacroTopicEntry;
import com.progetto.model.MultipleChoiceExercise;
import com.progetto.model.Progress;
import com.progetto.model.QuestionResult;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExercisesViewModel {

    private static String selectedTopic;
    private static String selectedQuestionId;

    public static void setSelectedTopic(String topic) {
        selectedTopic = topic;
    }
    public static String getSelectedTopic() {
        return selectedTopic;
    }
    public static void setSelectedQuestionId(String qid) {
        selectedQuestionId = qid;
    }
    public static String getSelectedQuestionId() {
        return selectedQuestionId;
    }

    private final ExerciseRepository exerciseRepository;
    private final UserRepository    userRepository;

    private final List<MultipleChoiceExercise> exercises = new ArrayList<>();
    private int currentIndex;
    private int wrongAnswersCount;

    private Timeline timer;
    private int elapsedSeconds;

    private final StringProperty questionText   = new SimpleStringProperty();
    private final ObservableList<String> options = FXCollections.observableArrayList();
    private final StringProperty selectedAnswer = new SimpleStringProperty();
    private final StringProperty timerText      = new SimpleStringProperty("00:00");

    public ExercisesViewModel() {
        exerciseRepository = new ExerciseRepository();
        userRepository     = new UserRepository();
    }

    public void loadExercises() {
        if (selectedTopic == null || selectedTopic.isBlank()) {
            throw new IllegalStateException("Nessun topic selezionato");
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
        if (exercises.isEmpty()) {
            throw new IllegalStateException("Nessun esercizio disponibile per il topic: " + selectedTopic);
        }
        currentIndex      = 0;
        wrongAnswersCount = 0;
        loadCurrentQuestion();
    }

    private void loadCurrentQuestion() {
        MultipleChoiceExercise ex = exercises.get(currentIndex);
        questionText.set(ex.getQuestion());
        options.setAll(ex.getOptions());
        setSelectedQuestionId(ex.getQuestionId());
        selectedAnswer.set("");
    }

    public boolean hasNext() {
        return currentIndex < exercises.size() - 1;
    }

    public void nextQuestion() {
        currentIndex++;
        loadCurrentQuestion();
    }

    public boolean submitAnswer() {
        MultipleChoiceExercise ex = exercises.get(currentIndex);
        boolean correct = ex.getCorrectAnswer().equals(selectedAnswer.get());
        if (!correct) {
            wrongAnswersCount++;
        }

        List<User> users = userRepository.getAllUsers();
        if (!users.isEmpty()) {
            User user = users.get(0);
            updateQuestionResult(user, ex.getQuestionId(), correct);
            userRepository.updateUser(user);
        }
        return correct;
    }

    private void updateQuestionResult(User user, String questionId, boolean correct) {
        List<Progress> progressList = user.getProgress();
        if (progressList == null) {
            progressList = new ArrayList<>();
            user.setProgress(progressList);
        }
        Progress prog = null;
        for (Progress p : progressList) {
            if (p.getMacroTopic().equals(selectedTopic)) {
                prog = p;
                break;
            }
        }
        if (prog == null) {
            prog = new Progress();
            prog.setMacroTopic(selectedTopic);
            prog.setQuestionResults(new ArrayList<>());
            prog.setScore(0);
            prog.setTime("0");
            prog.setPassed(false);
            progressList.add(prog);
        }

        List<QuestionResult> results = prog.getQuestionResults();
        QuestionResult qr = null;
        for (QuestionResult q : results) {
            if (q.getQuestionId().equals(questionId)) {
                qr = q;
                break;
            }
        }
        if (qr == null) {
            qr = new QuestionResult();
            qr.setQuestionId(questionId);
            qr.setAttempts(0);
            qr.setCorrect(false);
            results.add(qr);
        }

        qr.setAttempts(qr.getAttempts() + 1);
        if (correct) {
            qr.setCorrect(true);
        }
    }

    /**
     * Alla fine del quiz aggiorna:
     * 1) loginDates (lista di String ISO)
     * 2) tempo (mantiene il minore tra old/new)
     * 3) score (mantiene il maggiore tra old/new)
     * 4) passed (true se già passato o ora passato)
     */
    public void finishExercises() {
        stopTimer();
        int totalQs = exercises.size();
        int elapsed  = elapsedSeconds;

        List<User> users = userRepository.getAllUsers();
        if (users.isEmpty()) return;
        User user = users.get(0);

        // 1) loginDates
        List<String> dates = user.getLoginDates();
        if (dates == null) {
            dates = new ArrayList<>();
            user.setLoginDates(dates);
        }
        String today = LocalDate.now().toString();
        if (dates.isEmpty() || !dates.get(dates.size() - 1).equals(today)) {
            dates.add(today);
        }

        // 2–4) aggiorna Progress per il topic
        for (Progress prog : user.getProgress()) {
            if (!prog.getMacroTopic().equals(selectedTopic)) continue;

            // **time**: mantieni il minore
            int prevTime = 0;
            try { prevTime = Integer.parseInt(prog.getTime()); }
            catch (NumberFormatException ignored) {}
            if (prevTime == 0 || elapsed < prevTime) {
                prog.setTime(String.valueOf(elapsed));
            }

            // **score**: mantieni il maggiore
            int prevScore = prog.getScore();
            int newScore  = 0;
            for (QuestionResult q : prog.getQuestionResults()) {
                if (q.isCorrect()) newScore += 10;
            }
            if (newScore > prevScore) {
                prog.setScore(newScore);
            }

            // **passed**: rimane true se già era true, o se adesso è completo
            boolean alreadyPassed = prog.isPassed();
            boolean nowPassed     = prog.getQuestionResults().stream()
                    .filter(QuestionResult::isCorrect)
                    .count() == totalQs;
            prog.setPassed(alreadyPassed || nowPassed);

            break;
        }

        userRepository.updateUser(user);
    }

    // ─── TIMER ─────────────────────────────────────────────────────────────────────
    public void startTimer() {
        stopTimer();
        elapsedSeconds = 0;
        timerText.set("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                elapsedSeconds++;
                int m = elapsedSeconds / 60;
                int s = elapsedSeconds % 60;
                timerText.set(String.format("%02d:%02d", m, s));
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) timer.stop();
    }

    // ─── GETTERS per binding ───────────────────────────────────────────────────────
    public StringProperty questionTextProperty()   { return questionText;   }
    public ObservableList<String> getOptions()     { return options;        }
    public StringProperty selectedAnswerProperty() { return selectedAnswer; }
    public StringProperty timerTextProperty()      { return timerText;      }
    public int getWrongAnswersCount()              { return wrongAnswersCount; }
    public int getTotalQuestionsCount()            { return exercises.size();   }
    public String getCorrectAnswer()               { return exercises.get(currentIndex).getCorrectAnswer(); }
}
