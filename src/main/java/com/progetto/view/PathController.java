package com.progetto.view;

import com.progetto.viewmodel.PathViewModel;
import com.progetto.viewmodel.MacroTopicModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller per la visualizzazione del percorso degli argomenti.
 * Usa un layout zig-zag sulla parte sinistra con nodi di tipo card per migliorare l'UX.
 */
public class PathController implements Initializable {
    @FXML private Pane pathPane;
    @FXML private Label homeButton;
    @FXML private ImageView profileIcon;

    private final PathViewModel vm = new PathViewModel();
    private static final double NODE_WIDTH = 160;
    private static final double NODE_HEIGHT = 48;
    private static final double CORNER_RADIUS = 8;
    private static final double LINE_WIDTH = 4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vm.loadPathTopics();
        // Redraw on resize per mantenere proporzioni
        pathPane.widthProperty().addListener((obs, oldW, newW) -> drawPath());
        pathPane.heightProperty().addListener((obs, oldH, newH) -> drawPath());
        homeButton.setOnMouseClicked(this::navigateToHome);
        profileIcon.setOnMouseClicked(this::navigateToHome);
        drawPath();
    }

    /**
     * Disegna il percorso a zig-zag e i nodi come card con hover e ombra.
     */
    private void drawPath() {
        // Rimuove precedenti elementi
        pathPane.getChildren().removeIf(n -> "path-node".equals(n.getUserData()));

        List<MacroTopicModel> topics = vm.getTopics();
        if (topics.isEmpty()) return;

        double w = pathPane.getWidth();
        double h = pathPane.getHeight();
        // Spazio x per zig-zag: 20% e 40% larghezza
        double x1 = w * 0.2;
        double x2 = w * 0.4;
        double yStart = 60;
        double availableHeight = h - yStart - NODE_HEIGHT;
        double vSpacing = topics.size() > 1 ? availableHeight / (topics.size() - 1) : 0;

        // Calcola posizioni
        List<Point2D> centers = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            double x = (i % 2 == 0) ? x1 : x2;
            double y = yStart + i * vSpacing;
            centers.add(new Point2D(x, y));
        }

        // Disegna linee di collegamento
        for (int i = 0; i < centers.size() - 1; i++) {
            Point2D p1 = centers.get(i);
            Point2D p2 = centers.get(i + 1);
            Line horizontal = new Line(p1.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2,
                    p2.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2);
            horizontal.setStroke(Color.web("#B0BEC5"));
            horizontal.setStrokeWidth(LINE_WIDTH);
            horizontal.setStrokeLineCap(StrokeLineCap.ROUND);
            horizontal.setUserData("path-node");

            Line vertical = new Line(p2.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2,
                    p2.getX() + NODE_WIDTH/2, p2.getY() + NODE_HEIGHT/2);
            vertical.setStroke(Color.web("#B0BEC5"));
            vertical.setStrokeWidth(LINE_WIDTH);
            vertical.setStrokeLineCap(StrokeLineCap.ROUND);
            vertical.setUserData("path-node");

            pathPane.getChildren().addAll(horizontal, vertical);
        }

        // Disegna nodi come card
        for (int i = 0; i < centers.size(); i++) {
            MacroTopicModel m = topics.get(i);
            Point2D pos = centers.get(i);

            Rectangle rect = new Rectangle(pos.getX(), pos.getY(), NODE_WIDTH, NODE_HEIGHT);
            rect.setArcWidth(CORNER_RADIUS * 2);
            rect.setArcHeight(CORNER_RADIUS * 2);
            Color fill = m.isUnlocked() ? Color.web("#FCFCFF") : Color.web("#F5F5F5");
            rect.setFill(fill);
            rect.setStroke(m.isUnlocked() ? Color.web("#42A5F5") : Color.web("#CCCCCC"));
            rect.setStrokeWidth(2);
            rect.setEffect(new DropShadow(6, Color.gray(0, 0.25)));
            rect.setUserData("path-node");

            Label title = new Label(m.getTitle());
            title.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
            title.setTextFill(Color.web("#37474F"));
            title.setLayoutX(pos.getX() + 16);
            title.setLayoutY(pos.getY() + (NODE_HEIGHT - title.getFont().getSize()) / 2);
            title.setUserData("path-node");

            Group card = new Group(rect, title);
            card.setCursor(Cursor.HAND);
            card.setUserData("path-node");
            card.setOnMouseClicked(e -> onNodeClick(m, e));

            // Hover effect
            card.setOnMouseEntered(e -> rect.setFill(Color.web("#E3F2FD")));
            card.setOnMouseExited(e -> rect.setFill(fill));

            pathPane.getChildren().add(card);
        }
    }

    private void onNodeClick(MacroTopicModel m, MouseEvent event) {
        if (!m.isUnlocked()) {
            new Alert(Alert.AlertType.WARNING,
                    "Questo argomento non è ancora sbloccato.", ButtonType.OK)
                    .showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/ExercisesView.fxml")
            );
            Parent exercisesRoot = loader.load();
            com.progetto.viewmodel.ExercisesViewModel.setSelectedTopic(m.getTitle());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(exercisesRoot, 900, 600));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Impossibile caricare gli esercizi.", ButtonType.OK)
                    .showAndWait();
        }
    }

    private void navigateToHome(MouseEvent event) {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot, 900, 600));
            stage.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Impossibile tornare alla home.", ButtonType.OK)
                    .showAndWait();
        }
    }
}