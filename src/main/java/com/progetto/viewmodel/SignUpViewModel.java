package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SignUpViewModel {

    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty confirmPassword = new SimpleStringProperty("");
    private final UserRepository userRepository;

    public SignUpViewModel() {
        // Inizializza il repository degli utenti
        userRepository = new UserRepository();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }

    /**
     * Effettua la registrazione di un nuovo utente.
     * @return true se la registrazione ha successo, false in caso di errori (es. password non corrispondenti)
     */
    public boolean signUp() {
        if (!password.get().equals(confirmPassword.get())) {
            System.out.println("Errore: le password non corrispondono!");
            return false;
        }
        // Crea un nuovo utente con un id univoco (ad esempio basato sul timestamp)
        User newUser = new User();
        newUser.setId("u" + System.currentTimeMillis());
        newUser.setUsername(username.get());
        newUser.setPassword(password.get()); // In produzione, la password va hashata!
        newUser.setFirstName(name.get());
        // Altri campi (lastName, email, progress, ecc.) possono essere impostati qui se necessari

        userRepository.addUser(newUser);
        System.out.println("Utente " + username.get() + " registrato con successo.");
        return true;
    }
}
