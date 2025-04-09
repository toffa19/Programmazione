package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.progetto.util.SessionManager;

public class LoginViewModel {

    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final UserRepository userRepository;
    private String loggedUserId; // Per memorizzare l'id dell'utente che ha fatto login

    public LoginViewModel() {
        // Inizializza il repository degli utenti
        userRepository = new UserRepository();
    }

    // Proprietà per il binding con la UI
    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    /**
     * Effettua il login confrontando le credenziali con gli utenti salvati nel file JSON.
     * @return true se il login ha successo, false altrimenti.
     */
    public boolean login() {
        for (User user : userRepository.getAllUsers()) {
            // In un'app reale, la password dovrebbe essere confrontata in maniera sicura (es. hash con salt)
            if (user.getUsername().equals(username.get()) && user.getPassword().equals(password.get())) {
                loggedUserId = user.getId();
                // Imposta l'utente loggato nel SessionManager
                SessionManager.setCurrentUserId(loggedUserId);
                return true;
            }
        }
        System.out.println("Login failed for user: " + username.get());
        return false;
    }

}
