package view;

import controler.Algorithme;
import controler.Controller;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Map;
import model.Path;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Window extends Application {


    TextualView Tview;
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

        Tview = new TextualView(map, myPane);
    }

    public void Compute(ActionEvent event) {
        controller.computeOptimalTour();
        Gview.refreshMap();
        Tview.refreshTable();
    }

    public void Export(ActionEvent event) {
        TextInputDialog popup = new TextInputDialog();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        popup.setTitle("Duration");
        popup.setHeaderText("");
        popup.setContentText("Please enter the duration:");
        Optional<String> result = popup.showAndWait();
        result.ifPresent(duration -> System.out.println("Duration: " + duration));
    }

}
