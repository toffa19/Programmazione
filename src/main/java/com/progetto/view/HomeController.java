package com.progetto.view;

import com.progetto.viewmodel.ExercisesViewModel;
import com.progetto.viewmodel.HomeViewModel;
import com.progetto.viewmodel.MacroTopicModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {

    @FXML private VBox daysBox;              // Sezione sinistra per i giorni di slancio
    @FXML private Label precisionLabel;        // Label per la precisione
    @FXML private FlowPane macroTopicContainer; // Container per le card dei macroTopic
    @FXML private Button pathButton;           // Pulsante per la pagina Path
    @FXML private ImageView profileIcon;       // Icona profilo nell'header

    private final HomeViewModel viewModel = new HomeViewModel();

    @FXML
    public void initialize() {
        precisionLabel.textProperty().bind(viewModel.precisionProperty());
        viewModel.loadMacroTopics();
        createMacroTopicCards();

        for (int i = 1; i <= 5; i++) {
            Label dayLabel = new Label("Giorno " + i);
            daysBox.getChildren().add(dayLabel);
        }

        pathButton.setOnAction(e -> {
            try {
                Parent pathRoot = FXMLLoader.load(getClass().getResource("/view/PathView.fxml"));
                Stage stage = (Stage) pathButton.getScene().getWindow();
                stage.setScene(new Scene(pathRoot, 900, 600));
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        profileIcon.setOnMouseClicked(e -> {
            try {
                Parent profileRoot = FXMLLoader.load(getClass().getResource("/view/ProfileView.fxml"));
                Stage stage = (Stage) profileIcon.getScene().getWindow();
                stage.setScene(new Scene(profileRoot, 900, 600));
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        try {
            profileIcon.setImage(new Image(getClass().getResourceAsStream("/assets/images/profile.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createMacroTopicCards() {
        macroTopicContainer.getChildren().clear();
        viewModel.getMacroTopics().forEach(topic -> {
            VBox card = new VBox(5);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 10; "
                    + "-fx-border-radius: 5; -fx-background-radius: 5; "
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 0);");
            // Se il topic non è sbloccato, disabilita l'azione e applica uno stile diverso (es. opacità ridotta)
            if (!topic.isUnlocked()) {
                card.setStyle(card.getStyle() + " -fx-opacity: 0.5;");
                card.setOnMouseClicked(e -> {
                    // Visualizza un messaggio che il topic non è sbloccato
                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION,
                            "Completa il topic precedente per sbloccare questo livello!", javafx.scene.control.ButtonType.OK);
                    alert.showAndWait();
                });
            } else {
                card.setOnMouseClicked(e -> {
                    ExercisesViewModel.setSelectedTopic(topic.getTitle());
                    try {
                        Parent exercisesRoot = FXMLLoader.load(getClass().getResource("/view/ExercisesView.fxml"));
                        Stage stage = (Stage) card.getScene().getWindow();
                        stage.setScene(new Scene(exercisesRoot, 900, 600));
                        stage.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            ImageView imgView = new ImageView();
            imgView.setFitHeight(80);
            imgView.setFitWidth(80);
            if (topic.getImageUrl() != null && !topic.getImageUrl().isEmpty()) {
                imgView.setImage(new Image(topic.getImageUrl()));
            }
            Label titleLabel = new Label(topic.getTitle());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            card.getChildren().addAll(imgView, new Separator(), titleLabel);
            macroTopicContainer.getChildren().add(card);
        });
    }
}
