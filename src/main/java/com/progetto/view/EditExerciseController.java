package com.progetto.view;

import com.progetto.viewmodel.EditExerciseViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditExerciseController {

    @FXML private ComboBox<String> topicCombo;
    @FXML private ComboBox<String> levelCombo;
    @FXML private ComboBox<String> questionCombo;
    @FXML private TextArea questionArea;
    @FXML private TextField opt0, opt1, opt2, opt3;
    @FXML private ComboBox<String> correctCombo;
    @FXML private Button saveButton;

    private final EditExerciseViewModel vm = new EditExerciseViewModel();

    @FXML
    public void initialize() {
        // riempo i combo
        topicCombo.setItems(vm.getTopics());
        levelCombo.setItems(vm.getLevels());

        // bind selezioni → VM
        topicCombo.valueProperty().bindBidirectional(vm.selectedTopicProperty());
        levelCombo.valueProperty().bindBidirectional(vm.selectedLevelProperty());

        // al cambiare topic/level, aggiorna le questionId
        vm.selectedTopicProperty().addListener((o,old,n)-> vm.loadQuestionIds());
        vm.selectedLevelProperty().addListener((o,old,n)-> vm.loadQuestionIds());

        // questionCombo: usa getQuestionIds()
        questionCombo.setItems(vm.getQuestionIds());
        questionCombo.valueProperty().bindBidirectional(vm.selectedQuestionIdProperty());
        questionCombo.valueProperty().addListener((o,old,n)-> {
            vm.loadQuestion();
            questionArea.setText(vm.questionTextProperty().get());
            opt0.setText(vm.optionsProperty().get(0));
            opt1.setText(vm.optionsProperty().get(1));
            opt2.setText(vm.optionsProperty().get(2));
            opt3.setText(vm.optionsProperty().get(3));
            correctCombo.setItems(FXCollections.observableArrayList(vm.optionsProperty()));
            correctCombo.setValue(vm.correctAnswerProperty().get());
        });

        // bind campi di testo al VM per l’editing
        questionArea.textProperty().bindBidirectional(vm.questionTextProperty());
        opt0.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(0,n));
        opt1.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(1,n));
        opt2.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(2,n));
        opt3.textProperty().addListener((o,old,n)-> vm.optionsProperty().set(3,n));
        correctCombo.valueProperty().bindBidirectional(vm.correctAnswerProperty());

        saveButton.setOnAction(e -> vm.saveEdited());
    }
}
