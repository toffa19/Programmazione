// src/main/java/com/progetto/viewmodel/HomeViewModel.java
package com.progetto.viewmodel;

import com.progetto.model.QuestionResult;
import com.progetto.model.User;
import com.progetto.repository.ExerciseRepository;
import com.progetto.repository.UserRepository;
import com.progetto.util.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeViewModel {

    private final UserRepository userRepo = new UserRepository();
    private final ExerciseRepository exRepo = new ExerciseRepository();

    private final StringProperty consecutiveDays   = new SimpleStringProperty("0");
    private final StringProperty completionPercent = new SimpleStringProperty("0%");
    private final StringProperty rank              = new SimpleStringProperty("-");
    private final ObservableList<ScoreModel> leaderboard = FXCollections.observableArrayList();

    public StringProperty consecutiveDaysProperty() { return consecutiveDays; }
    public StringProperty completionProperty()     { return completionPercent; }
    public StringProperty rankProperty()           { return rank; }
    public ObservableList<ScoreModel> getLeaderboard() { return leaderboard; }

    public void loadStatsAndLeaderboard() {
        String currentUserId = SessionManager.getCurrentUserId();
        User user = userRepo.getAllUsers().stream()
                .filter(u -> currentUserId.equals(u.getId()))
                .findFirst()
                .orElse(new User());

        computeConsecutiveDays(user.getId());
        computeCompletion(user);
        loadLeaderboard();
        computeRank(currentUserId);
    }

    private void computeConsecutiveDays(String userId) {
        List<LocalDate> logins = SessionManager.getLoginDates(userId);
        LocalDate today = LocalDate.now();
        int streak = 0;
        while (logins.contains(today.minusDays(streak))) {
            streak++;
        }
        consecutiveDays.set(String.valueOf(streak));
    }

    private void computeCompletion(User user) {
        Set<String> allTopics = exRepo.getAllExercises().stream()
                .map(e -> e.getMacroTopic())
                .collect(Collectors.toSet());
        long passed = 0;
        if (user.getProgress() != null) {
            passed = user.getProgress().stream()
                    .filter(p -> p.isPassed())
                    .count();
        }
        int pct = allTopics.isEmpty() ? 0 : (int)(passed * 100.0 / allTopics.size());
        completionPercent.set(pct + "%");
    }

    private void loadLeaderboard() {
        leaderboard.clear();
        for (User u : userRepo.getAllUsers()) {
            int pts = 0;
            if (u.getProgress() != null) {
                for (var prog : u.getProgress()) {
                    if (prog.getQuestionResults() != null) {
                        for (QuestionResult qr : prog.getQuestionResults()) {
                            if (qr.isCorrect()) pts += 10;
                        }
                    }
                }
            }
            // Passo ora userId, username e punti: risolve il mismatch di costruttore :contentReference[oaicite:0]{index=0}:contentReference[oaicite:1]{index=1}
            leaderboard.add(new ScoreModel(u.getId(), u.getUsername(), pts));
        }
        FXCollections.sort(leaderboard, Comparator.comparingInt(ScoreModel::getPoints).reversed());
    }

    private void computeRank(String userId) {
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).getUserId().equals(userId)) {
                rank.set((i + 1) + "°");
                return;
            }
        }
        rank.set("-");
    }
}
