package com.progetto.view;

import com.progetto.viewmodel.SignUpViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField nameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private RadioButton studentRadio;
    @FXML private RadioButton editorRadio;
    @FXML private Button signUpButton;
    @FXML private Button goToLoginButton;

    private final SignUpViewModel viewModel = new SignUpViewModel();

    @FXML
    public void initialize() {
        // binding bidirezionale
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        usernameField.textProperty().bindBidirectional(viewModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(viewModel.passwordProperty());
        confirmPasswordField.textProperty().bindBidirectional(viewModel.confirmPasswordProperty());

        // ToggleGroup per i ruoli
        ToggleGroup group = new ToggleGroup();
        studentRadio.setToggleGroup(group);
        editorRadio.setToggleGroup(group);
        // default su "Studente"
        studentRadio.setSelected(true);

        // bind scelto radio -> roleProperty
        group.selectedToggleProperty().addListener((obs, oldT, newT) -> {
            if (newT == editorRadio) viewModel.roleProperty().set("editor");
            else                      viewModel.roleProperty().set("student");
        });

        signUpButton.setOnAction(e -> {
            if (viewModel.signUp()) {
                try {
                    Parent homeRoot = FXMLLoader.load(
                            getClass().getResource("/view/LoginView.fxml")
                    );
                    Stage stage = (Stage) signUpButton.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 400, 400));
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                new Alert(Alert.AlertType.ERROR,
                        "Registrazione fallita: password non corrispondono",
                        ButtonType.OK).showAndWait();
            }
        });

        goToLoginButton.setOnAction(e -> {
            try {
                Parent loginRoot = FXMLLoader.load(
                        getClass().getResource("/view/LoginView.fxml")
                );
                Stage stage = (Stage) goToLoginButton.getScene().getWindow();
                stage.setScene(new Scene(loginRoot, 400, 400));
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
