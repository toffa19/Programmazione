package com.progetto.view;

import com.progetto.viewmodel.DeleteExerciseViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class DeleteExerciseController {

    @FXML private ComboBox<String> topicCombo;
    @FXML private ComboBox<String> levelCombo;
    @FXML private ComboBox<String> questionCombo;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;

    private final DeleteExerciseViewModel vm = new DeleteExerciseViewModel();

    @FXML
    public void initialize() {
        topicCombo.setItems(vm.getTopics());
        levelCombo.setItems(vm.getLevels());
        questionCombo.setItems(vm.getQuestionIds());

        topicCombo.valueProperty().bindBidirectional(vm.selectedTopicProperty());
        levelCombo.valueProperty().bindBidirectional(vm.selectedLevelProperty());
        questionCombo.valueProperty().bindBidirectional(vm.selectedQuestionIdProperty());

        // aggiorna le domande quando cambio topic o livello
        vm.selectedTopicProperty().addListener((o,old,n)-> vm.loadQuestionIds());
        vm.selectedLevelProperty().addListener((o,old,n)-> vm.loadQuestionIds());

        deleteButton.setOnAction(e -> {
            vm.deleteSelected();
            new Alert(Alert.AlertType.INFORMATION, "Esercizio eliminato.").showAndWait();
        });
        cancelButton.setOnAction(e -> {
            // torna a EditorHome
            try {
                Parent home = FXMLLoader.load(getClass().getResource("/view/EditorHomeView.fxml"));
                Stage stage = (Stage)((Node)cancelButton).getScene().getWindow();
                stage.setScene(new Scene(home, 800, 600));
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
