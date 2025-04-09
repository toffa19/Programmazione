package com.progetto.util;

public class SessionManager {
    private static String currentUserId;

    public static String getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
    }
}
