package com.progetto.view;

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
        // Binding bidirezionale
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());

        loginButton.setOnAction(e -> {
            if (viewModel.login()) {
                // Login riuscito: naviga verso la Home
                try {
                    Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 900, 600));
                    // 900x600 è un esempio di dimensioni, modificale a tuo piacimento
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // Mostra un messaggio di errore
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
}
