package com.progetto.viewmodel;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SignUpViewModel {

    private final StringProperty name             = new SimpleStringProperty("");
    private final StringProperty username         = new SimpleStringProperty("");
    private final StringProperty password         = new SimpleStringProperty("");
    private final StringProperty confirmPassword  = new SimpleStringProperty("");
    private final StringProperty role             = new SimpleStringProperty("student");

    private final UserRepository userRepository = new UserRepository();

    public StringProperty nameProperty()            { return name; }
    public StringProperty usernameProperty()        { return username; }
    public StringProperty passwordProperty()        { return password; }
    public StringProperty confirmPasswordProperty() { return confirmPassword; }
    public StringProperty roleProperty()            { return role; }

    public boolean signUp() {
        if (!password.get().equals(confirmPassword.get())) {
            System.out.println("Errore: le password non corrispondono!");
            return false;
        }
        User newUser = new User();
        newUser.setId("u" + System.currentTimeMillis());
        newUser.setUsername(username.get());
        newUser.setPassword(password.get());
        newUser.setFirstName(name.get());
        newUser.setRole(role.get());          // <— imposto il ruolo scelto

        userRepository.addUser(newUser);
        System.out.println("Utente " + username.get() + " registrato con ruolo " + role.get());
        return true;
    }
}
