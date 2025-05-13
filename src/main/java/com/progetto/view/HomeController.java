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

public class HomeController {

    @FXML private Button homeButton;
    @FXML private Button pathButton;
    @FXML private ImageView profileIcon;

    @FXML private Label daysLabel;
    @FXML private Label completionLabel;

    @FXML private TableView<ScoreModel> leaderboardTable;
    @FXML private TableColumn<ScoreModel, String> userCol;
    @FXML private TableColumn<ScoreModel, Integer> pointsCol;

    private final HomeViewModel viewModel = new HomeViewModel();

    @FXML
    public void initialize() {
        // setto colonne
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        // carico dati e classifica
        viewModel.loadStatsAndLeaderboard();
        daysLabel.textProperty().bind(viewModel.consecutiveDaysProperty());
        completionLabel.textProperty().bind(viewModel.completionProperty());
        leaderboardTable.setItems(viewModel.getLeaderboard());

        // Home → PathView (o rimani su Home, a seconda della logica)
        homeButton.setOnAction(e -> { /* già su Home: nulla */ });

        // Path → PathView.fxml
        pathButton.setOnAction(e -> {
            try {
                Parent pathRoot = FXMLLoader.load(getClass().getResource("/view/PathView.fxml"));
                Stage st = (Stage) pathButton.getScene().getWindow();
                st.setScene(new Scene(pathRoot, 900, 600));
                st.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Profilo
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

        // carico icona
        profileIcon.setImage(new Image(getClass().getResourceAsStream("/assets/images/profile.png")));
    }
}
