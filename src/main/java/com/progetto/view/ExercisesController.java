// src/main/java/com/progetto/view/ExercisesController.java
package com.progetto.view;

import com.progetto.viewmodel.ExercisesViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExercisesController {

    @FXML private Label topicLabel;
    @FXML private Label questionLabel;
    @FXML private VBox optionsContainer;
    @FXML private Button nextButton;
    @FXML private Label timerLabel;
    @FXML private Button exitButton;

    private final ExercisesViewModel viewModel = new ExercisesViewModel();

    @FXML
    public void initialize() {
        topicLabel.setText(ExercisesViewModel.getSelectedTopic());

        questionLabel.textProperty().bind(viewModel.questionTextProperty());
        timerLabel.textProperty().bind(viewModel.timerTextProperty());

        viewModel.loadExercises();
        showCurrentQuestion();
        viewModel.startTimer();

        nextButton.setOnAction(evt -> {
            boolean corretto = viewModel.submitAnswer();

            // Recupero risposta corretta
            String rispostaCorretta = viewModel.getCorrectAnswer();

            // Creo il popup di feedback
            Alert feedback = new Alert(Alert.AlertType.INFORMATION);
            feedback.setHeaderText(corretto ? "Risposta corretta!" : "Risposta sbagliata!");
            int wrong = viewModel.getWrongAnswersCount();
            int total = viewModel.getTotalQuestionsCount();

            // Contenuto del popup con risposta corretta
            feedback.setContentText(
                    (corretto ? "" : "La risposta giusta era: " + rispostaCorretta + "\n") +
                            "Hai sbagliato " + wrong + " su " + total + " domande."
            );
            feedback.showAndWait();

            if (viewModel.hasNext()) {
                viewModel.nextQuestion();
                showCurrentQuestion();
            } else {
                // ...
            }
        });

        exitButton.setOnAction(evt -> {
            viewModel.stopTimer();
            navigateToHome();
        });
    }

    /** Ritorna alla HomeView */
    private void navigateToHome() {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(homeRoot, 900, 600));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void showCurrentQuestion() {
        optionsContainer.getChildren().clear();
        ToggleGroup group = new ToggleGroup();
        for (String opt : viewModel.getOptions()) {
            RadioButton rb = new RadioButton(opt);
            rb.setToggleGroup(group);
            rb.setOnAction(e -> viewModel.selectedAnswerProperty().set(opt));
            optionsContainer.getChildren().add(rb);
        }
    }

}
