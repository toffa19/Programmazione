// src/main/java/com/progetto/view/HomeController.java
package com.progetto.view;

import com.progetto.viewmodel.HomeViewModel;
import com.progetto.viewmodel.ScoreModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML private Button pathButton;
    @FXML private ImageView profileIcon;

    @FXML private Label daysLabel;
    @FXML private Label completionLabel;
    @FXML private Label rankLabel;        // aggiunto per mostrare la posizione

    @FXML private TableView<ScoreModel> leaderboardTable;
    @FXML private TableColumn<ScoreModel, String> userCol;
    @FXML private TableColumn<ScoreModel, Integer> pointsCol;

    private final HomeViewModel viewModel = new HomeViewModel();

    @FXML
    public void initialize() {
        // setto colonne come prima :contentReference[oaicite:2]{index=2}:contentReference[oaicite:3]{index=3}
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        // carico tutti i dati
        viewModel.loadStatsAndLeaderboard();

        // bind delle label (streak, completion, rank)
        daysLabel.textProperty().bind(viewModel.consecutiveDaysProperty());
        completionLabel.textProperty().bind(viewModel.completionProperty());
        rankLabel.textProperty().bind(viewModel.rankProperty());

        // popolo la tabella (già ordinata in viewModel)
        leaderboardTable.setItems(viewModel.getLeaderboard());

        // navigazione verso PathView
        pathButton.setOnAction(e -> {
            try {
                Parent pathRoot = FXMLLoader.load(getClass().getResource("/view/PathView.fxml"));
                Stage st = (Stage) pathButton.getScene().getWindow();
                st.setScene(new Scene(pathRoot, 900, 600));
                st.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // click sull’icona profilo
        profileIcon.setOnMouseClicked(e -> {
            try {
                Parent pf = FXMLLoader.load(getClass().getResource("/view/ProfileView.fxml"));
                Stage st = (Stage) profileIcon.getScene().getWindow();
                st.setScene(new Scene(pf, 900, 600));
                st.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // carico immagine profilo
        profileIcon.setImage(new Image(
                getClass().getResourceAsStream("/assets/images/profile.png")
        ));
    }
}
