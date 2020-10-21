package view;

import controler.Algorithme;
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
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Intersection;
import model.Map;
import model.Path;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Window extends Application {

    GraphicalView Gview;
    TextualView Tview = new TextualView();
    Map map;
    Controller controller;

    @FXML
    private Canvas canvas;
    @FXML
    private Pane overlay;
    @FXML
    private Pane myPane;
    @FXML
    private Button btn_load_requests;

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
        Gview.reloadMap();
        //canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());;


    }

    public void LoadMap(ActionEvent event) {

        map = new Map();
        Gview = new GraphicalView(map,canvas,overlay); //Creation de la vue graphique à partir de la map et de la zone d'affichage

        //Chargement map
        FileChooser mapFileChooser = new FileChooser();
        mapFileChooser.setTitle("Load Map");
        mapFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File mapFile = mapFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        
        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseMapXML(mapFile.getAbsolutePath(), map);

        //Dessin map
        Gview.drawMap();

        ((Node) event.getSource()).setDisable(true);
        btn_load_requests.setDisable(false);
    }

    public void LoadRequests(ActionEvent event) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
        System.out.println(map.getListRequests().get(0).getDeliveryPoint());

        Gview.reloadMap();
        
        Tview.createRequestList(map, myPane);
        ((Node) event.getSource()).setDisable(true);
    }

    public void Compute(ActionEvent event) {
        Algorithme algo = new Algorithme(map);
        HashMap<Long, List<Path>> mapSmallestPaths = algo.computeSmallestPaths();
        map.setMapSmallestPaths(mapSmallestPaths);
        Gview.reloadMap();

        
        

    }

}
