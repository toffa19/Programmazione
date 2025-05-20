package com.progetto.view;

import com.progetto.util.SessionManager;
import com.progetto.viewmodel.ProfileViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EditorProfileController {

    @FXML private Button homeButton;
    @FXML private Button logoutButton;
    @FXML private ImageView profileImage;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;

    @FXML private TextField usernameField;
    @FXML private TextField fullNameField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;

    @FXML private Button editButton;
    @FXML private Button saveButton;

    private final ProfileViewModel viewModel = new ProfileViewModel();

    @FXML
    public void initialize() {
        // 1) Carico l’avatar
        profileImage.setImage(new Image(
                getClass().getResourceAsStream("/assets/images/profile.png")
        ));

        // 2) Popolo labels e campi con i dati dell’utente loggato
        fullNameLabel.setText(viewModel.getFullName());
        emailLabel.setText(viewModel.getEmail());

        usernameField.setText(viewModel.getUsername());
        fullNameField.setText(viewModel.getFullName());


        // 3) Disabilito subito i campi in lettura
        setEditMode(false);

        // 4) Wiring dei pulsanti
        homeButton.setOnAction(e -> onHome());
        logoutButton.setOnAction(e -> onLogout());
        editButton.setOnAction(e -> onEdit());
        saveButton.setOnAction(e -> onSave());
    }

    @FXML
    private void onHome() {
        navigateTo("/view/EditorHomeView.fxml", 900, 600);
    }

    @FXML
    private void onLogout() {
        SessionManager.setCurrentUserId(null);
        SessionManager.setCurrentUserRole(null);
        navigateTo("/view/LoginView.fxml", 400, 400);
    }

    @FXML
    private void onEdit() {
        setEditMode(true);
        currentPasswordField.clear();
        newPasswordField.clear();
    }

    @FXML
    private void onSave() {
        String enteredCurrent = currentPasswordField.getText();
        String enteredNew     = newPasswordField.getText();

        // Cambio password se richiesto
        if (!enteredNew.isBlank()) {
            if (!enteredCurrent.equals(viewModel.getPassword())) {
                new Alert(Alert.AlertType.ERROR, "Password attuale errata.").showAndWait();
                return;
            }
            viewModel.setPassword(enteredNew);
        }

        // Aggiorno viewModel con i nuovi valori
        viewModel.setUsername(usernameField.getText());
        viewModel.setFullName(fullNameField.getText());

        viewModel.saveUserData();

        // Ripristino la visualizzazione in sola lettura
        fullNameLabel.setText(viewModel.getFullName());
        emailLabel.setText(viewModel.getEmail());
        usernameField.setText(viewModel.getUsername());


        setEditMode(false);
        currentPasswordField.clear();
        newPasswordField.clear();

        new Alert(Alert.AlertType.INFORMATION, "Profilo aggiornato.").showAndWait();
    }

    private void setEditMode(boolean editing) {
        usernameField.setDisable(!editing);
        fullNameField.setDisable(!editing);
        currentPasswordField.setDisable(!editing);
        newPasswordField.setDisable(!editing);
        saveButton.setDisable(!editing);
        editButton.setDisable(editing);
    }

    private void navigateTo(String fxmlPath, int w, int h) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage st = (Stage) homeButton.getScene().getWindow();
            st.setScene(new Scene(root, w, h));
            st.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Errore caricamento view:\n" + ex.getMessage())
                    .showAndWait();
        }
    }
}
