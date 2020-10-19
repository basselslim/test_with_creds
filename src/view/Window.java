package view;

import controler.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Map;

import java.io.IOException;

public class Window extends Application {

    GraphicalView Gview = new GraphicalView();
    TextualView Tview = new TextualView();
    Map map;
    Controller controller;

    @FXML
    private Canvas canvas;
    @FXML
    private Pane overlay;

    @Override
    public void start(Stage MainFrame) throws Exception {
        initUI(MainFrame);
        map = new Map();
        controller = new Controller(map);
    }

    private void initUI(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));

        var scene = new Scene(root, 1650, 1050, Color.WHITE);

        stage.setTitle("DeliveryTool");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }


    public void LoadMap(ActionEvent event) {

        Gview.drawMap(map,canvas,overlay);
        Gview.drawLines(canvas, overlay);
        Gview.drawShapes(canvas);
    }
}

