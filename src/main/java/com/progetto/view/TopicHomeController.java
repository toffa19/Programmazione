package com.progetto.view;

import com.progetto.viewmodel.TopicHomeViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class TopicHomeController {

    @FXML private VBox pathContainer;
    @FXML private Label topicTitle;
    @FXML private Label topicDescription;

    private final TopicHomeViewModel viewModel = new TopicHomeViewModel();

    @FXML
    public void initialize() {
        // Carica i dati del topic selezionato (già salvato in un campo statico, come in ExercisesViewModel)
        viewModel.loadTopicDetails();

        // Mostra nome e descrizione del topic
        topicTitle.setText(viewModel.getTopicName());
        topicDescription.setText(viewModel.getTopicDescription());

        // Popola la sezione sinistra con le domande
        viewModel.getQuestionIds().forEach(questionId -> {
            // Crea un pulsante o un piccolo box
            Button questionButton = new Button(questionId);
            questionButton.setStyle("-fx-pref-width: 180;");

            // Se l'utente ha già risposto correttamente, potresti aggiungere un'icona di check
            if (viewModel.isQuestionCompleted(questionId)) {
                questionButton.setText(questionId + " (✓)");
            }

            questionButton.setOnAction(e -> {
                // Naviga alla schermata ExercisesView, passando la singola domanda
                // Oppure potresti passare l'indice della domanda
                viewModel.setSelectedQuestion(questionId);
                try {
                    Parent exercisesRoot = FXMLLoader.load(getClass().getResource("/view/ExercisesView.fxml"));
                    Stage stage = (Stage) questionButton.getScene().getWindow();
                    stage.setScene(new Scene(exercisesRoot, 900, 600));
                    stage.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            pathContainer.getChildren().addAll(questionButton, new Separator());
        });
    }
}
