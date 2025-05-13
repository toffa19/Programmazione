package com.progetto.view;

import com.progetto.util.SessionManager;
import com.progetto.viewmodel.ProfileViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private Button homeButton;
    @FXML private Button logoutButton;      // ← nuovo
    @FXML private ImageView profileImage;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;

    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField countryField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private ComboBox<String> languageComboBox;

    @FXML private Button editButton;
    @FXML private Button saveButton;

    private final ProfileViewModel viewModel = new ProfileViewModel();

    @FXML
    public void initialize() {
        // … carica dati, imposta view/edit mode, ecc.

        homeButton.setOnAction(e -> navigateTo("/view/HomeView.fxml"));
        logoutButton.setOnAction(e -> {
            // Pulisce sessione e torna al login
            SessionManager.setCurrentUserId(null);
            navigateTo("/view/LoginView.fxml");
        });

        editButton.setOnAction(e -> setEditMode(true));
        saveButton.setOnAction(e -> {
            // … salva dati
            viewModel.saveUserData();
            setEditMode(false);
        });
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage st = (Stage) homeButton.getScene().getWindow();
            st.setScene(new Scene(root, 900, 600));
            st.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setEditMode(boolean editing) {
        fullNameField.setDisable(!editing);
        usernameField.setDisable(!editing);
        countryField.setDisable(!editing);
        languageComboBox.setDisable(!editing);
        newPasswordField.setDisable(!editing);
        currentPasswordField.setDisable(true);

        editButton.setDisable(editing);
        saveButton.setDisable(!editing);
    }
}
