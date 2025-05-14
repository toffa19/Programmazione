package com.progetto.view;

import com.progetto.util.SessionManager;
import com.progetto.viewmodel.ProfileViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProfileController {

    @FXML private Button homeButton;
    @FXML private Button logoutButton;
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
        // 1) Popola il form con i dati correnti
        viewModel.loadUserData();
        fullNameLabel.setText(viewModel.getFullName());
        emailLabel.setText(viewModel.getEmail());
        usernameField.setText(viewModel.getUsername());
        fullNameField.setText(viewModel.getFullName());
        countryField.setText(viewModel.getCountry());

        // Combo lingue
        List<String> langs = List.of("Italiano","English","Español","Français");
        languageComboBox.setItems(FXCollections.observableArrayList(langs));
        languageComboBox.getSelectionModel().select(viewModel.getLanguage());

        // Profile image (placeholder)
        profileImage.setImage(new Image(
                getClass().getResourceAsStream("/assets/images/profile.png")
        ));

        // 2) Navigazione
        homeButton.setOnAction(e -> navigateTo("/view/HomeView.fxml"));
        logoutButton.setOnAction(e -> {
            SessionManager.setCurrentUserId(null);
            navigateTo("/view/LoginView.fxml");
        });

        // 3) Edit/Save mode
        editButton.setOnAction(e -> setEditMode(true));
        saveButton.setOnAction(e -> {
            // aggiorna viewModel
            viewModel.setFullName(fullNameField.getText());
            viewModel.setUsername(usernameField.getText());
            viewModel.setCountry(countryField.getText());
            viewModel.setLanguage(languageComboBox.getValue());
            if (!newPasswordField.getText().isBlank()) {
                viewModel.setPassword(newPasswordField.getText());
            }
            viewModel.saveUserData();           // scrive su JSON :contentReference[oaicite:1]{index=1}:contentReference[oaicite:2]{index=2}
            // aggiorna UI
            fullNameLabel.setText(viewModel.getFullName());
            usernameField.setText(viewModel.getUsername());
            setEditMode(false);
        });
    }

    private void setEditMode(boolean editing) {
        fullNameField.setDisable(!editing);
        usernameField.setDisable(!editing);
        countryField.setDisable(!editing);
        languageComboBox.setDisable(!editing);
        newPasswordField.setDisable(!editing);
        saveButton.setDisable(!editing);
        editButton.setDisable(editing);
    }

    private void navigateTo(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage st = (Stage) homeButton.getScene().getWindow();
            st.setScene(new Scene(root, 900, 600));
            st.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
