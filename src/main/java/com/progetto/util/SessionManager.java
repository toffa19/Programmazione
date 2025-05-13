package com.progetto.util;

import java.time.LocalDate;
import java.util.*;

public class SessionManager {
    private static String currentUserId;

    // mappa userId → liste di date di accesso
    private static final Map<String, List<LocalDate>> loginDates = new HashMap<>();

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
        // al momento del set, registriamo il login odierno
        recordLogin(userId);
    }

    /** Registra la data odierna per l’utente */
    public static void recordLogin(String userId) {
        loginDates
                .computeIfAbsent(userId, k -> new ArrayList<>())
                .add(LocalDate.now());
    }

    /**
     * Ritorna la lista delle date in cui l’utente ha fatto login
     * (in memoria per sessione)
     */
    public static List<LocalDate> getLoginDates(String userId) {
        return loginDates.getOrDefault(userId, Collections.emptyList());
    }
}
