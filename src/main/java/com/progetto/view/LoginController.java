package com.progetto.view;

import com.progetto.util.SessionManager;
import com.progetto.viewmodel.LoginViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
                String role = SessionManager.getCurrentUserRole();
                try {
                    // Carico la vista corretta in base al ruolo
                    String fxml = "editor".equals(role)
                            ? "/view/EditorHomeView.fxml"
                            : "/view/HomeView.fxml";
                    Parent homeRoot = FXMLLoader.load(
                            getClass().getResource(fxml)
                    );
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 900, 600));
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Errore nel caricamento della pagina.",
                            ButtonType.OK
                    ).showAndWait();
                }
            } else {
                new Alert(
                        Alert.AlertType.ERROR,
                        "Credenziali non valide!",
                        ButtonType.OK
                ).showAndWait();
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
