// src/main/java/com/progetto/view/PathController.java
package com.progetto.view;

import com.progetto.viewmodel.MacroTopicModel;
import com.progetto.viewmodel.PathViewModel;
import com.progetto.viewmodel.ExercisesViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
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

public class PathController implements Initializable {
    @FXML private Pane pathPane;
    @FXML private Label homeButton;
    @FXML private ImageView profileIcon;

    private final PathViewModel vm = new PathViewModel();

    private static final double NODE_WIDTH     = 160;
    private static final double NODE_HEIGHT    = 48;
    private static final double CORNER_RADIUS  = 8;
    private static final double LINE_WIDTH     = 4;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) chiedi al VM di caricare i topic
        vm.loadPathTopics();
        // 2) disegna il percorso
        drawPath();

        // ridisegna al ridimensionare
        pathPane.widthProperty().addListener((o,oldV,newV)-> drawPath());
        pathPane.heightProperty().addListener((o,oldV,newV)-> drawPath());

        // navigazione home/profile
        homeButton.setOnMouseClicked(this::navigateToHome);
        profileIcon.setOnMouseClicked(this::navigateToHome);
    }

    private void drawPath() {
        // rimuovi disegni precedenti
        pathPane.getChildren().removeIf(n -> "path-node".equals(n.getUserData()));

        List<MacroTopicModel> topics = vm.getTopics();
        if (topics.isEmpty()) return;

        double w = pathPane.getWidth();
        double h = pathPane.getHeight();
        double x1 = w * 0.2;
        double x2 = w * 0.4;
        double yStart = 60;
        double availableHeight = h - yStart - NODE_HEIGHT;
        double vSpacing = topics.size() > 1 ? availableHeight / (topics.size() - 1) : 0;

        // calcola i centri
        List<Point2D> centers = new ArrayList<>();
        for (int i = 0; i < topics.size(); i++) {
            double x = (i % 2 == 0) ? x1 : x2;
            double y = yStart + i * vSpacing;
            centers.add(new Point2D(x, y));
        }

        // disegna linee di collegamento
        for (int i = 0; i < centers.size() - 1; i++) {
            Point2D p1 = centers.get(i);
            Point2D p2 = centers.get(i + 1);

            Line hLine = new Line(
                    p1.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2,
                    p2.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2
            );
            hLine.setStroke(Color.web("#B0BEC5"));
            hLine.setStrokeWidth(LINE_WIDTH);
            hLine.setStrokeLineCap(StrokeLineCap.ROUND);
            hLine.setUserData("path-node");

            Line vLine = new Line(
                    p2.getX() + NODE_WIDTH/2, p1.getY() + NODE_HEIGHT/2,
                    p2.getX() + NODE_WIDTH/2, p2.getY() + NODE_HEIGHT/2
            );
            vLine.setStroke(Color.web("#B0BEC5"));
            vLine.setStrokeWidth(LINE_WIDTH);
            vLine.setStrokeLineCap(StrokeLineCap.ROUND);
            vLine.setUserData("path-node");

            pathPane.getChildren().addAll(hLine, vLine);
        }

        // disegna i nodi come card
        for (int i = 0; i < topics.size(); i++) {
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

            final String topicTitle = m.getTitle();  // variabile effectively final
            card.setOnMouseClicked(e -> onNodeClick(topicTitle, e));

            // hover effect
            card.setOnMouseEntered(e -> rect.setFill(Color.web("#E3F2FD")));
            card.setOnMouseExited(e -> rect.setFill(fill));

            pathPane.getChildren().add(card);
        }
    }

    private void onNodeClick(String topicTitle, MouseEvent event) {
        // segna passed e sblocca
        vm.markTopicPassed(topicTitle);

        // imposta topic selezionato
        ExercisesViewModel.setSelectedTopic(topicTitle);

        try {
            // 1) Verifica che il resource path sia corretto
            URL fxmlUrl = getClass().getResource("/view/ExercisesView.fxml");
            System.out.println("DEBUG: ExercisesView.fxml URL = " + fxmlUrl);  // stampa null se il file non c'è

            // 2) Carica l'FXML
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
            st.setScene(new Scene(root, 900, 600));
            st.show();

        } catch (IOException ex) {
            // 3) Stampa lo stack trace per capire esattamente l’errore
            ex.printStackTrace();

            // 4) Mostra in alert anche il messaggio dell’eccezione
            new Alert(Alert.AlertType.ERROR,
                    "Impossibile caricare gli esercizi:\n" + ex.getMessage(),
                    ButtonType.OK)
                    .showAndWait();
        }
    }

    private void navigateToHome(MouseEvent event) {
        try {
            Parent home = FXMLLoader.load(getClass().getResource("/view/HomeView.fxml"));
            Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
            st.setScene(new Scene(home, 900, 600));
            st.show();
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "Impossibile tornare alla home.", ButtonType.OK).showAndWait();
        }
    }
}
