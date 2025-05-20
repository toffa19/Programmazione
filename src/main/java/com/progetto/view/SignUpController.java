package com.progetto.view;

import com.progetto.model.User;
import com.progetto.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.util.UUID;

public class SignUpController {

    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button signUpButton;
    @FXML private Button goToLoginButton;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        // impostiamo i possibili ruoli
        roleCombo.setItems(FXCollections.observableArrayList("student", "editor"));
    }

    @FXML
    private void onSignUp() {
        String email    = emailField.getText().trim();
        String role     = roleCombo.getValue();
        String username = usernameField.getText().trim();
        String pwd      = passwordField.getText();
        String confirm  = confirmPasswordField.getText();

        if (email.isEmpty() || role == null || username.isEmpty() || pwd.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Compila tutti i campi (incluso ruolo).").showAndWait();
            return;
        }
        if (!pwd.equals(confirm)) {
            new Alert(Alert.AlertType.ERROR, "Le password non corrispondono.").showAndWait();
            return;
        }

        // Creo nuovo utente, includendo il ruolo
        User u = new User();
        u.setId(UUID.randomUUID().toString());
        u.setEmail(email);
        u.setUsername(username);
        u.setFirstName(username);  // manteniamo username anche come firstName
        u.setPassword(pwd);
        u.setRole(role);           // assegno il ruolo scelto

        userRepo.addUser(u);
        new Alert(Alert.AlertType.INFORMATION, "Registrazione avvenuta con successo!").showAndWait();
        goToLogin();
    }

    @FXML
    private void onGoToLogin() {
        goToLogin();
    }

    private void goToLogin() {
        try {
            Parent login = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Stage stage = (Stage) goToLoginButton.getScene().getWindow();
            stage.setScene(new Scene(login, 400, 400));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Impossibile aprire login.").showAndWait();
        }
    }
}
