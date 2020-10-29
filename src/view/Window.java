package view;

import controler.Algorithme;
import controler.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Map;
import model.Path;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Window extends Application {


    TextualView Tview = new TextualView();
    Map map = new Map();
    GraphicalView Gview;
    Controller controller = new Controller(map);

    @FXML
    private Pane overlay;
    @FXML
    private Pane myPane;
    @FXML
    private Button btn_load_requests;

    @Override
    public void start(Stage MainFrame) throws Exception {
        initUI(MainFrame);

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

    }

    public void UnZoom(ActionEvent event) {
        Gview.unZoom();
    }

    public void LoadMap(ActionEvent event) {


        Gview = new GraphicalView(map, overlay); //Creation de la vue graphique à partir de la map et de la zone d'affichage

        //Load the map
        controller.LoadMap(event);

        //Draw the map
        Gview.refreshMap();

        Gview.enableSelection();
        //reactivate Requests button
        btn_load_requests.setDisable(false);
    }

    public void LoadRequests(ActionEvent event) {
        controller.LoadRequests(event);

        Gview.refreshMap();

        Tview.createRequestList(map, myPane);
    }

    public void Compute(ActionEvent event) {
        Algorithme algo = new Algorithme(map);
        HashMap<Long, List<Path>> mapSmallestPaths = algo.computeSmallestPaths();
        map.setMapSmallestPaths(mapSmallestPaths);
        Gview.refreshMap();


    }

}
