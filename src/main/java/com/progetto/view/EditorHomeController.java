package com.progetto.view;

import com.progetto.util.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class EditorHomeController {

    @FXML private Button createExerciseButton;
    @FXML private Button editExercisesButton;
    @FXML private Button deleteExercisesButton;    // ← nuovo
    @FXML private Button logoutButton;

    @FXML
    public void initialize() {
        // eventuali binding o setup iniziale
    }

    @FXML
    private void onCreateExercise() throws IOException {
        URL fxmlUrl = getClass().getClassLoader()
                .getResource("view/CreateExerciseView.fxml");
        if (fxmlUrl == null) {
            throw new IOException("FXML non trovato in classpath: view/CreateExerciseView.fxml");
        }
        Parent pane = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)createExerciseButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onEditExercises() throws IOException {
        Parent pane = FXMLLoader.load(
                getClass().getResource("/view/EditExerciseView.fxml")
        );
        Stage stage = (Stage) ((Node) editExercisesButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onDeleteExercises() throws IOException {
        // Carica la vista per eliminare esercizi
        URL fxmlUrl = getClass().getClassLoader()
                .getResource("view/DeleteExerciseView.fxml");
        if (fxmlUrl == null) {
            throw new IOException("FXML non trovato in classpath: view/DeleteExerciseView.fxml");
        }
        Parent pane = FXMLLoader.load(fxmlUrl);
        Stage stage = (Stage)((Node)deleteExercisesButton).getScene().getWindow();
        stage.setScene(new Scene(pane, 800, 600));
        stage.show();
    }

    @FXML
    private void onLogout() throws IOException {
        SessionManager.setCurrentUserId(null);
        SessionManager.setCurrentUserRole(null);

        Parent loginRoot = FXMLLoader.load(
                getClass().getResource("/view/LoginView.fxml")
        );
        Stage stage = (Stage) ((Node) logoutButton).getScene().getWindow();
        stage.setScene(new Scene(loginRoot, 400, 400));
        stage.show();
    }
}
