package com.progetto.view;

import com.progetto.viewmodel.ExercisesViewModel;
import com.progetto.viewmodel.PathViewModel;
import com.progetto.viewmodel.MacroTopicModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PathController {

    @FXML private Button homeButton;    // pulsante header per tornare alla Home
    @FXML private VBox pathContainer;   // left pane
    @FXML private ImageView profileIcon;

    private final PathViewModel viewModel = new PathViewModel();

    @FXML
    public void initialize() {
        // Home
        homeButton.setOnAction(e -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
                Stage st = (Stage) homeButton.getScene().getWindow();
                st.setScene(new Scene(root, 900, 600));
                st.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Profile icon
        profileIcon.setOnMouseClicked(e -> {
            try {
                Parent pf = FXMLLoader.load(getClass().getResource("/view/ProfileView.fxml"));
                Stage st = (Stage) profileIcon.getScene().getWindow();
                st.setScene(new Scene(pf, 900, 600));
                st.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        profileIcon.setImage(new Image(getClass().getResourceAsStream("/assets/images/profile.png")));

        // Popolo il percorso
        viewModel.loadPathTopics();
        viewModel.getTopics().forEach(topic -> {
            VBox box = new VBox(5);
            box.getStyleClass().add("topic-node");

            Label name = new Label(topic.getTitle());
            Separator sep = new Separator();

            if (!topic.isUnlocked()) {
                box.setOpacity(0.4);
                box.setOnMouseClicked(evt -> {
                    new Alert(Alert.AlertType.INFORMATION,
                            "Completa il topic precedente per sbloccare questo topic!",
                            ButtonType.OK).showAndWait();
                });
            } else {
                box.setOnMouseClicked(evt -> {
                    ExercisesViewModel.setSelectedTopic(topic.getTitle());
                    try {
                        Parent ex = FXMLLoader.load(getClass().getResource("/view/ExercisesView.fxml"));
                        Stage st = (Stage) box.getScene().getWindow();
                        st.setScene(new Scene(ex, 900, 600));
                        st.show();
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                });
            }

            box.getChildren().addAll(name, sep);
            pathContainer.getChildren().add(box);
        });
    }
}
