package com.progetto.view;

import com.progetto.model.Question;
import com.progetto.viewmodel.ExercisesViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ExercisesController {

    @FXML private Label topicLabel;          // Visualizza il nome del topic
    @FXML private Button backButton;           // Bottone "Home"
    @FXML private Button exitButton;           // Bottone "Esci dall’esercizio"
    @FXML private ScrollPane scrollPane;       // ScrollPane per il container degli esercizi
    @FXML private VBox exercisesContainer;     // Contenitore che accumula le domande

    private final ExercisesViewModel viewModel = new ExercisesViewModel();

    @FXML
    public void initialize() {
        topicLabel.setText(viewModel.getSelectedTopic());
        viewModel.startTimer();

        backButton.setOnAction(e -> navigateToHome());
        exitButton.setOnAction(e -> {
            viewModel.stopTimer();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Hai interrotto l'esercizio. Tentativo non completato!", ButtonType.OK);
            alert.showAndWait();
            navigateToHome();
        });

        viewModel.loadQuestionsForTopic();
        appendCurrentQuestion();
    }

    /**
     * Appende la domanda corrente in coda al container, mantenendo quelle già visualizzate.
     */
    private void appendCurrentQuestion() {
        Question currentQuestion = viewModel.getCurrentQuestion();
        if (currentQuestion == null) {
            viewModel.stopTimer();
            // Aggiorna il tempo per il macrotopic salvando il tempo totale impiegato,
            // tenendo il tempo migliore (minore) se già presente
            viewModel.updateTopicTime();
            showSummary();
            return;
        }

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label questionLabel = new Label(currentQuestion.getQuestion());
        questionLabel.setWrapText(true);
        questionLabel.setStyle("-fx-text-fill: #333;");

        ToggleGroup group = new ToggleGroup();
        VBox optionsBox = new VBox(5);
        for (String option : currentQuestion.getOptions()) {
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
            boolean correct = currentQuestion.getCorrectAnswer().equalsIgnoreCase(selected.getText());
            String feedback = correct ? "Corretto!" : "Sbagliato! La risposta corretta è: " + currentQuestion.getCorrectAnswer();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, feedback, ButtonType.OK);
            alert.showAndWait();

            viewModel.updateUserProgress(currentQuestion.getQuestionId(), correct);
            viewModel.processAnswerResult(correct);

            if (viewModel.getErrorCount() > 3) {
                viewModel.stopTimer();
                Alert failAlert = new Alert(Alert.AlertType.ERROR, "Non hai superato l'esercizio!", ButtonType.OK);
                failAlert.showAndWait();
                navigateToHome();
                return;
            } else {
                viewModel.moveToNextQuestion();
                appendCurrentQuestion();
                scrollPane.setVvalue(1.0);
            }
        });

        box.getChildren().addAll(questionLabel, optionsBox, submitButton);
        exercisesContainer.getChildren().add(box);
        scrollPane.setVvalue(0.0);
    }

    private void navigateToHome() {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
            Stage stage = (Stage) topicLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot, 900, 600));
            stage.show();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showSummary() {
        int totalQuestions = viewModel.getQuestions().size();
        int errors = viewModel.getErrorCount();
        int correctAnswers = totalQuestions - errors; // oppure ricalcola esattamente dal progresso
        int score = (int)((correctAnswers / (double) totalQuestions) * 100);
        int timeTaken = viewModel.getElapsedSeconds();

        String summary = "Risultati dell'esercizio:\n" +
                "Domande totali: " + totalQuestions + "\n" +
                "Risposte corrette: " + correctAnswers + "\n" +
                "Errori: " + errors + "\n" +
                "Punteggio: " + score + "\n" +
                "Tempo impiegato: " + timeTaken + " secondi";
        Alert summaryAlert = new Alert(Alert.AlertType.INFORMATION, summary, ButtonType.OK);
        summaryAlert.showAndWait();

        navigateToHome();
    }
}
