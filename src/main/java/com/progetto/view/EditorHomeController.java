package com.progetto.view;

import com.progetto.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EditorHomeController {

    @FXML private Button createExerciseButton;
    @FXML private Button editExercisesButton;
    @FXML private Button deleteExercisesButton;
    @FXML private Button logoutButton;
    @FXML private ImageView profileIcon;

    @FXML
    public void initialize() {
        // carico immagine profilo
        profileIcon.setImage(new Image(
                getClass().getResourceAsStream("/assets/images/profile.png")
        ));

        // click sull’icona profilo: apro la View di profilo
        profileIcon.setOnMouseClicked(e -> {
            // questo è il **nome corretto** del FXML:
            URL fxmlUrl = getClass().getResource("/view/EditorProfileView.fxml");
            if (fxmlUrl == null) {
                System.err.println("[ERROR] Non trovo EditorProfileView.fxml nel classpath!");
                return;
            }
            try {
                Parent pf = FXMLLoader.load(fxmlUrl);
                Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                stage.setScene(new Scene(pf, 900, 600));
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void onCreateExercise() throws IOException {
        URL fxmlUrl = getClass().getResource("/view/CreateExerciseView.fxml");
        if (fxmlUrl == null) throw new IOException("FXML non trovato: CreateExerciseView.fxml");
        Parent pane = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)createExerciseButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onEditExercises() throws IOException {
        URL fxmlUrl = getClass().getResource("/view/EditExerciseView.fxml");
        if (fxmlUrl == null) throw new IOException("FXML non trovato: EditExerciseView.fxml");
        Parent pane = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)editExercisesButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onDeleteExercises() throws IOException {
        URL fxmlUrl = getClass().getResource("/view/DeleteExerciseView.fxml");
        if (fxmlUrl == null) throw new IOException("FXML non trovato: DeleteExerciseView.fxml");
        Parent pane = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)deleteExercisesButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onLogout() throws IOException {
        SessionManager.setCurrentUserId(null);
        SessionManager.setCurrentUserRole(null);
        URL fxmlUrl = getClass().getResource("/view/LoginView.fxml");
        if (fxmlUrl == null) throw new IOException("FXML non trovato: LoginView.fxml");
        Parent loginRoot = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)logoutButton).getScene().getWindow();
        stage.setScene(new Scene(loginRoot, 400, 400));
        stage.show();
    }
}
