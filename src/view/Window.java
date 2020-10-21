package view;

import controler.Controller;
import controler.XMLLoader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Intersection;
import model.Map;

import java.io.File;
import java.io.IOException;

public class Window extends Application {

    @FXML
    private Canvas canvas;
    @FXML
    private Pane overlay;
    @FXML
    private Pane myPane;

    GraphicalView Gview = new GraphicalView();
    TextualView Tview = new TextualView();
    Map map;
    Controller controller;

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

    public void Zoom(ActionEvent event) {
        Gview.zoom();
        //canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());;
        //Gview.drawMap(map,canvas,overlay);

    }

    public void LoadMap(ActionEvent event) {
        map = new Map();
        FileChooser mapFileChooser = new FileChooser();
        mapFileChooser.setTitle("Load Map");
        mapFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File mapFile = mapFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseMapXML(mapFile.getAbsolutePath(), map);
        map.display();
        Intersection intersection = map.getListIntersections().get(25303831);
        System.out.println(intersection);

        Gview.drawMap(map,canvas,overlay);
        //Gview.drawShapes(canvas);

        ((Node) event.getSource()).setDisable(true);
    }

    public void LoadRequests(ActionEvent event) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
        Tview.createRequestList(map, myPane);

        ((Node) event.getSource()).setDisable(true);
    }

}
