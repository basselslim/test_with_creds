package view;

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


import java.io.IOException;
import javafx.stage.StageStyle;
import model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Window extends Application {


    TextualView Tview;
    Map map = new Map();
    GraphicalView Gview;
    Controller controller = new Controller(map);
    MouseGestures mg = new MouseGestures(controller);

    @FXML
    private Pane overlay;
    @FXML
    private Pane myPane;
    @FXML
    private Button btn_load_requests;
    @FXML
    private javafx.scene.control.TextArea TextArea;

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
        Gview = new GraphicalView(map, overlay, mg);
        Tview = new TextualView(map, myPane, TextArea);
        controller.setGview(Gview);
        controller.setTview(Tview);
        map.addObserver(Gview);

        //Load the map
        controller.LoadMap(event);

        //reactivate Requests button
        btn_load_requests.setDisable(false);
        TextArea.setText("Please load a request list");
    }

    public void LoadRequests(ActionEvent event) {
        map.addObserver(Tview);
        controller.LoadRequests(event);
        Gview.enableSelection();
    }

    public void addRequest(ActionEvent event) {
        controller.addRequest();
        controller.confirmRequest(); //TEMPORAIRE

    }

    public void undo(ActionEvent event){
        controller.undo();
    }

    public void redo(ActionEvent event){
        controller.redo();
    }

    public void Compute(ActionEvent event) {
        controller.computeTour();
    }

    public void Export(ActionEvent event) {

        controller.ExportRoadMap(event);
    }

}
