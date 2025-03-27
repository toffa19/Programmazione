package com.progetto.view;

import com.progetto.model.Question;
import com.progetto.viewmodel.ExercisesViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExercisesController {

    @FXML private Label topicLabel;
    @FXML private ListView<String> pathListView; // mostra le questionId
    @FXML private ScrollPane scrollPane;
    @FXML private VBox questionContainer; // qui inseriamo la domanda corrente

    private final ExercisesViewModel viewModel = new ExercisesViewModel();

    @FXML
    public void initialize() {
        // Imposta il topic nell'header
        topicLabel.setText(viewModel.getSelectedTopic());

        // Carica le domande dal JSON
        viewModel.loadQuestionsForTopic();

        // Popola il path a sinistra
        pathListView.setItems(viewModel.getQuestionIds());

        // Imposta come primo selezionato (se presente) l'indice 0
        if (!viewModel.getQuestionIds().isEmpty()) {
            pathListView.getSelectionModel().select(0);
            showQuestion(0);
        }

        // Quando l'utente seleziona una domanda dal path, mostriamo quella domanda
        pathListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            showQuestion(index);
        });
    }

    /**
     * Mostra la domanda all'indice specificato
     */
    private void showQuestion(int index) {
        questionContainer.getChildren().clear();
        viewModel.setCurrentQuestionIndex(index);

        Question question = viewModel.getCurrentQuestion();
        if (question == null) {
            return;
        }

        Label questionLabel = new Label(question.getQuestion());
        questionLabel.setWrapText(true);

        ToggleGroup group = new ToggleGroup();
        VBox optionsBox = new VBox(5);
        for (String option : question.getOptions()) {
            RadioButton rb = new RadioButton(option);
            rb.setToggleGroup(group);
            rb.setStyle("-fx-text-fill: black;");
            optionsBox.getChildren().add(rb);
        }

        Button submitButton = new Button("Submit Answer");
        submitButton.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Seleziona una risposta!", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            boolean correct = question.getCorrectAnswer().equalsIgnoreCase(selected.getText());
            String feedback = correct ? "Corretto!" : "Sbagliato! La risposta corretta è: " + question.getCorrectAnswer();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, feedback, ButtonType.OK);
            alert.showAndWait();

            // Aggiorna il JSON dell'utente (salva il progresso)
            viewModel.updateUserProgress(question.getQuestionId(), correct);

            // Se vuoi passare automaticamente alla prossima domanda:
            int nextIndex = viewModel.getCurrentQuestionIndex() + 1;
            if (nextIndex < viewModel.getQuestionIds().size()) {
                pathListView.getSelectionModel().select(nextIndex);
            } else {
                Alert doneAlert = new Alert(Alert.AlertType.INFORMATION, "Hai completato le domande per questo topic!", ButtonType.OK);
                doneAlert.showAndWait();
                // Naviga alla Home
                try {
                    Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
                    Stage stage = (Stage) topicLabel.getScene().getWindow();
                    stage.setScene(new Scene(homeRoot, 900, 600));
                    stage.show();
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        questionContainer.getChildren().addAll(questionLabel, optionsBox, submitButton);
        scrollPane.setVvalue(0.0); // torna in alto all'inizio della domanda
    }
}
