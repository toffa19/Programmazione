package com.progetto.view;

import com.progetto.repository.ExerciseRepository;
import com.progetto.viewmodel.CreateExerciseViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class CreateExerciseController {
    @FXML private ComboBox<String> topicCombo;
    @FXML private ComboBox<String> levelCombo;
    @FXML private TextField idField;
    @FXML private TextArea questionArea;
    @FXML private TextField opt0, opt1, opt2, opt3;
    @FXML private ComboBox<String> correctCombo;
    @FXML private Button createExButton;
    @FXML private VBox rootVBox;
    @FXML private Button addAnotherButton;    // ← nuovo
    @FXML private ScrollPane scrollPane;
    @FXML private VBox formsContainer;
    @FXML private javafx.scene.control.Button saveAllButton;
    private final CreateExerciseViewModel vm = new CreateExerciseViewModel();
    @FXML private Button homeButton;       // ← nuovo

    @FXML public void initialize() {
        // Ogni click aggiunge una nuova scheda **vuota**
        addAnotherButton.setOnAction(e -> addForm());

        topicCombo.setItems(vm.getTopics());
        levelCombo.setItems(vm.getLevels());
        topicCombo.valueProperty().bindBidirectional(vm.selectedTopicProperty());
        levelCombo.valueProperty().bindBidirectional(vm.selectedLevelProperty());
        idField.textProperty().bindBidirectional(vm.questionIdProperty());
        questionArea.textProperty().bindBidirectional(vm.questionTextProperty());

        // initialize options
        vm.optionsProperty().set(FXCollections.observableArrayList("", "", "", ""));
        opt0.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(0,n));
        opt1.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(1,n));
        opt2.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(2,n));
        opt3.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(3,n));

        correctCombo.itemsProperty().bind(vm.optionsProperty());
        correctCombo.valueProperty().bindBidirectional(vm.correctAnswerProperty());

        createExButton.setOnAction(e -> vm.createAndExit());

        // binding esistenti…
        createExButton.setOnAction(e -> {
            vm.createAndExit();
            // opzionale: se vuoi chiudere/navigare via FXMLLoader.load(...)
        });
        addAnotherButton.setOnAction(e -> vm.createAndContinue());

        homeButton.setOnAction(e -> {
            closeAndGoBack(e);
        });
    }

    // Chiude questa finestra e ritorna a EditorHomeView.fxml
    private void closeAndGoBack(javafx.event.Event e) {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/view/EditorHomeView.fxml"));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(home, 800, 600));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Impossibile tornare alla home.").showAndWait();
        }
    }

    private void addForm() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/ExerciseForm.fxml")
            );
            Node form = loader.load();

            // qui sostituisci la riga sbagliata…
            // ExerciseRepository controller = loader.saveExercises();

            // …con queste due:
            ExerciseRepository controller = new ExerciseRepository();
            controller.saveExercises();    // <— ora chiami il saveExercises() su repo

            form.setUserData(controller);
            formsContainer.getChildren().add(form);

            scrollPane.layout();
            scrollPane.setVvalue(1.0);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
