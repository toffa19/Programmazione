package com.progetto.view;

import com.progetto.viewmodel.SignUpViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button signUpButton;
    @FXML private Button goToLoginButton;

    private final SignUpViewModel viewModel = new SignUpViewModel();

    @FXML
    public void initialize() {
        // Binding bidirezionale tra i controlli della UI e le proprietà del ViewModel
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        confirmPasswordField.textProperty().bindBidirectional(viewModel.confirmPasswordProperty());

        signUpButton.setOnAction(e -> {
            if (viewModel.signUp()) {
                // Dopo la registrazione, torna alla schermata di login
                try {
                    Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    stage.setScene(new Scene(loginRoot, 400, 400));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        goToLoginButton.setOnAction(e -> {
            try {
                Parent loginRoot = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
                Stage stage = (Stage) goToLoginButton.getScene().getWindow();
                stage.setScene(new Scene(loginRoot, 400, 400));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
