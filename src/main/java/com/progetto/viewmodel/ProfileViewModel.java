package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;

public class ProfileViewModel {
    private final UserRepository userRepository;
    private User currentUser; // Utente corrente caricato dal JSON

    public ProfileViewModel() {
        userRepository = new UserRepository();
        // Per scopi dimostrativi, usiamo il primo utente della lista come "utente loggato"
        if (!userRepository.getAllUsers().isEmpty()) {
            currentUser = userRepository.getAllUsers().get(0);
        } else {
            // Se non ci sono utenti, creiamo un utente vuoto (gestione migliorabile in produzione)
            currentUser = new User();
        }
    }

    public void loadUserData() {
        // I dati sono già presenti in currentUser
    }

    // Full Name (usiamo firstName come nome)
    public String getFullName() {
        return currentUser.getFirstName() != null ? currentUser.getFirstName() : "";
    }
    public void setFullName(String fullName) {
        currentUser.setFirstName(fullName);
    }

    // NickName (campo aggiunto)
    public String getNickName() {
        return currentUser.getNickName() != null ? currentUser.getNickName() : "";
    }
    public void setNickName(String nickName) {
        currentUser.setNickName(nickName);
    }

    // Country (campo aggiunto)
    public String getCountry() {
        return currentUser.getCountry() != null ? currentUser.getCountry() : "";
    }
    public void setCountry(String country) {
        currentUser.setCountry(country);
    }

    // Language (campo aggiunto)
    public String getLanguage() {
        return currentUser.getLanguage() != null ? currentUser.getLanguage() : "";
    }
    public void setLanguage(String language) {
        currentUser.setLanguage(language);
    }

    // Password
    public String getPassword() {
        return currentUser.getPassword() != null ? currentUser.getPassword() : "";
    }
    public void setPassword(String password) {
        currentUser.setPassword(password);
    }

    public String getEmail() {
        return currentUser.getEmail() != null ? currentUser.getEmail() : "";
    }
    // L'email non viene modificata in questo esempio

    /**
     * Salva i dati del profilo aggiornando il JSON degli utenti.
     */
    public void saveUserData() {
        userRepository.updateUser(currentUser);
        System.out.println("Saving user data: "
                + getFullName() + ", "
                + getNickName() + ", "
                + getCountry() + ", "
                + getLanguage());
    }
}
