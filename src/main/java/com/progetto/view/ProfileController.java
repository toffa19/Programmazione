package com.progetto.view;

import com.progetto.viewmodel.ProfileViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ProfileController {

    @FXML private ImageView profileImage;
    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;

    @FXML private TextField fullNameField;
    @FXML private TextField nickNameField;
    @FXML private TextField genderField;
    @FXML private TextField countryField;
    @FXML private TextField timeZoneField;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private PasswordField currentPasswordField; // Campo per mostrare la password corrente (disabilitato)
    @FXML private PasswordField newPasswordField;     // Campo per inserire la nuova password

    @FXML private Button editButton;
    @FXML private Button saveButton;

    private final ProfileViewModel viewModel = new ProfileViewModel();

    @FXML
    public void initialize() {
        // Carica i dati dell'utente dal JSON
        viewModel.loadUserData();

        // Imposta le label in modalità "view"
        fullNameLabel.setText(viewModel.getFullName());
        emailLabel.setText(viewModel.getEmail());

        // Imposta i campi di testo con i dati correnti
        fullNameField.setText(viewModel.getFullName());
        nickNameField.setText(viewModel.getNickName());
        // Se desideri mostrare il genere, aggiorna anche il modello User e il relativo metodo getter/setter
        countryField.setText(viewModel.getCountry());
        currentPasswordField.setText(viewModel.getPassword());
        // Popola il ComboBox con alcune lingue configurate
        languageComboBox.getItems().addAll("English", "Italian", "Spanish", "German");
        languageComboBox.setValue(viewModel.getLanguage());

        // Carica l'immagine di profilo dal classpath
        try {
            profileImage.setImage(new Image(getClass().getResourceAsStream("/assets/images/profile.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Modalità di default: visualizzazione (campi non editabili)
        setEditMode(false);

        // Gestione pulsanti
        editButton.setOnAction(e -> setEditMode(true));
        saveButton.setOnAction(e -> {
            // Se viene inserita una nuova password, la utilizziamo; altrimenti si mantiene quella corrente.
            if (!newPasswordField.getText().isEmpty()) {
                viewModel.setPassword(newPasswordField.getText());
            }
            viewModel.setFullName(fullNameField.getText());
            viewModel.setNickName(nickNameField.getText());
            viewModel.setCountry(countryField.getText());
            viewModel.setLanguage(languageComboBox.getValue());
            // Se hai implementato il genere e il fuso orario, aggiorna anche questi campi:
            // viewModel.setGender(genderField.getText());
            // viewModel.setTimeZone(timeZoneField.getText());

            // Salva i dati nel JSON
            viewModel.saveUserData();

            // Aggiorna le label
            fullNameLabel.setText(viewModel.getFullName());
            // Ritorna in modalità "view"
            setEditMode(false);
        });
    }

    private void setEditMode(boolean editing) {
        fullNameField.setDisable(!editing);
        nickNameField.setDisable(!editing);
        genderField.setDisable(!editing);
        countryField.setDisable(!editing);
        timeZoneField.setDisable(!editing);
        languageComboBox.setDisable(!editing);
        // Il campo della password corrente rimane disabilitato
        currentPasswordField.setDisable(true);
        newPasswordField.setDisable(!editing);

        editButton.setDisable(editing);
        saveButton.setDisable(!editing);
    }
}
