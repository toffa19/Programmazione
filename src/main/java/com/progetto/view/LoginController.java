package com.progetto.view;

import com.progetto.util.SessionManager;
import com.progetto.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button goToSignUpButton;

    private final LoginViewModel viewModel = new LoginViewModel();

    @FXML
    public void initialize() {
        // Binding bidirezionale per i campi di testo
        // (Assicurati che LoginViewModel abbia proprietà adeguate oppure usa setText/getText)
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());

        loginButton.setOnAction(e -> {
            if (viewModel.login()) {
                // L'utente loggato è già memorizzato nel SessionManager dal viewModel.login()
                System.out.println("Login successful for user: " + SessionManager.getCurrentUserId());
                try {
                    Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 900, 600));
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Credenziali non valide!", ButtonType.OK);
                alert.showAndWait();
            }
        });

        goToSignUpButton.setOnAction(e -> {
            try {
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("/view/SignUpView.fxml"));
                Stage stage = (Stage) goToSignUpButton.getScene().getWindow();
                stage.setScene(new Scene(signUpRoot, 400, 400));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    // Una semplice utility per il binding bidirezionale
    // (Implementazione semplificata; in alternativa usa le proprietà JavaFX nel LoginViewModel)
    private static class SimpleStringPropertyWrapper extends javafx.beans.property.SimpleStringProperty {
        public SimpleStringPropertyWrapper(java.util.function.Supplier<String> getter, java.util.function.Consumer<String> setter) {
            super(getter.get());
            addListener((obs, oldVal, newVal) -> setter.accept(newVal));
        }
    }
}
