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

    private final ExercisesViewModel vm = new ExercisesViewModel();

    @FXML
    public void initialize() {
        try {
            // deve essere stato chiamato prima: ExercisesViewModel.setSelectedTopic(...)
            topicLabel.setText(ExercisesViewModel.getSelectedTopic());

            vm.loadExercises();
            vm.startTimer();
            bindUI();
            showCurrentQuestion();

            nextButton.setOnAction(e -> onNext());
            exitButton.setOnAction(e -> {
                vm.stopTimer();
                navigateToHome();
            });

        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText("Impossibile caricare gli esercizi.");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
            navigateToHome();
        }
    }

    private void bindUI() {
        questionLabel.textProperty().bind(vm.questionTextProperty());
        timerLabel.textProperty().bind(vm.timerTextProperty());
    }

    private void showCurrentQuestion() {
        optionsContainer.getChildren().clear();
        ToggleGroup group = new ToggleGroup();
        for (String opt : vm.getOptions()) {
            RadioButton rb = new RadioButton(opt);
            rb.setToggleGroup(group);
            rb.setOnAction(e -> vm.selectedAnswerProperty().set(opt));
            optionsContainer.getChildren().add(rb);
        }
    }

    private void onNext() {
        boolean corretto = vm.submitAnswer();
        Alert feedback = new Alert(Alert.AlertType.INFORMATION);
        feedback.setHeaderText(corretto ? "Risposta corretta!" : "Risposta sbagliata!");
        if (!corretto) {
            feedback.setContentText("La risposta giusta era: " + vm.getCorrectAnswer());
        }
        feedback.showAndWait();

        if (vm.hasNext()) {
            vm.nextQuestion();
            showCurrentQuestion();
        } else {
            vm.finishExercises();
            navigateToHome();
        }
    }

    private void navigateToHome() {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(home, 900, 600));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
