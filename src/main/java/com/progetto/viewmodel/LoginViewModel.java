package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.progetto.util.SessionManager;

import java.util.List;

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
        String inputUser = username.get();
        String inputPass = password.get();

        System.out.println("[DEBUG] Login attempt for username: '" + inputUser + "'");

        List<User> all = userRepository.getAllUsers();
        System.out.println("[DEBUG] Number of users in repository: " + all.size());

        for (User user : all) {
            System.out.println("[DEBUG] Checking user: '" + user.getUsername() +
                    "' (expected pass='" + user.getPassword() + "')");

            if (user.getUsername().equals(inputUser)) {
                System.out.println("[DEBUG] Username match for: " + inputUser);

                if (user.getPassword().equals(inputPass)) {
                    System.out.println("[DEBUG] Password match for user: " + inputUser);

                    // Setto sessione
                    loggedUserId = user.getId();
                    SessionManager.setCurrentUserId(loggedUserId);
                    SessionManager.setCurrentUserRole(user.getRole());

                    System.out.println("[DEBUG] Login successful: userId=" + loggedUserId +
                            ", role=" + user.getRole());
                    return true;
                } else {
                    System.out.println("[DEBUG] Password mismatch for user: " + inputUser);
                    return false;
                }
            }
        }

        System.out.println("[DEBUG] No user found with username: '" + inputUser + "'");
        return false;
    }


}
