package com.progetto.view;

import com.progetto.viewmodel.ExercisesViewModel;
import com.progetto.viewmodel.PathViewModel;
import com.progetto.viewmodel.TopicModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PathController {

    @FXML private VBox pathContainer;          // Sezione sinistra per il percorso
    @FXML private VBox topicDetailContainer;     // Sezione destra per i dettagli del topic
    @FXML private Label topicNameLabel;          // Label per il nome del topic
    @FXML private Label topicDescriptionLabel;   // Label per la descrizione del topic
    @FXML private Label lessonCountLabel;        // Label per il numero di lezioni
    @FXML private Label practiceCountLabel;      // Label per il numero di esercizi/practice
    @FXML private ImageView profileIcon;         // Icona profilo nell'header

    private final PathViewModel viewModel = new PathViewModel();

    @FXML
    public void initialize() {
        // Carica la lista dei topic in ordine
        viewModel.loadPathTopics();
        // Crea i nodi grafici per il percorso
        createPathNodes();
        // Se esiste almeno un topic, mostra i dettagli del primo
        if (!viewModel.getTopics().isEmpty()) {
            showTopicDetails(viewModel.getTopics().get(0));
        }

        // Gestione del click sull'icona profilo (navigazione alla pagina di profilo)
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

        // Imposta l'immagine del profilo dal classpath
        try {
            profileIcon.setImage(new Image(getClass().getResourceAsStream("/assets/images/profile.png")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createPathNodes() {
        pathContainer.getChildren().clear();
        var topics = viewModel.getTopics();

        for (TopicModel topic : topics) {
            // Crea un nodo per rappresentare il topic
            VBox topicBox = new VBox(5);
            topicBox.getStyleClass().add("topic-node");
            if (!topic.isUnlocked()) {
                topicBox.getStyleClass().add("topic-locked");
            }
            Label nameLabel = new Label(topic.getName());
            topicBox.getChildren().add(nameLabel);

            // Imposta l'azione: se il topic è sbloccato, naviga agli esercizi; altrimenti mostra un avviso
            topicBox.setOnMouseClicked(e -> {
                if (!topic.isUnlocked()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                            "Completa il topic precedente per sbloccare questo topic!", ButtonType.OK);
                    alert.showAndWait();
                } else {
                    System.out.println("Navigazione agli esercizi per il topic: " + topic.getName());
                    // Imposta il topic selezionato nel ViewModel degli esercizi
                    ExercisesViewModel.setSelectedTopic(topic.getName());
                    try {
                        Parent exercisesRoot = FXMLLoader.load(getClass().getResource("/view/ExercisesView.fxml"));
                        Stage stage = (Stage) topicBox.getScene().getWindow();
                        stage.setScene(new Scene(exercisesRoot, 900, 600));
                        stage.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Aggiunge il nodo e un separatore al container
            pathContainer.getChildren().addAll(topicBox, new Separator());
        }
    }

    /**
     * Mostra i dettagli del topic selezionato nella parte destra della pagina.
     */
    private void showTopicDetails(TopicModel topic) {
        topicNameLabel.setText(topic.getName());
        topicDescriptionLabel.setText(topic.getDescription());
        lessonCountLabel.setText(topic.getLessonCount() + " Lessons");
        practiceCountLabel.setText(topic.getPracticeCount() + " Practice");
    }
}
