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
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Map;
import model.Path;
import model.Segment;
import model.Tour;


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

        double length1 = 1.1;
        String streetName1 = "Rue du 0 au 1";
        long destination1 = 1;
        Segment s1 = new Segment(length1,streetName1,destination1);

        double length2 = 2.2;
        String streetName2 = "Rue du 1 au 2";
        long destination2 = 2;
        Segment s2 = new Segment(length2,streetName2,destination2);

        double length3 = 3.3;
        String streetName3 = "Rue du 2 au 3";
        long destination3 = 3;
        Segment s3 = new Segment(length3,streetName3,destination3);

        double length4 = 4.4;
        String streetName4 = "Rue du 3 au 4";
        long destination4 = 4;
        Segment s4 = new Segment(length4,streetName4,destination4);

        ArrayList<Segment> listSegments1 = new ArrayList<>();
        ArrayList<Segment> listSegments2 = new ArrayList<>();

        listSegments1.add(s1);
        listSegments1.add(s2);
        listSegments2.add(s3);
        listSegments2.add(s4);

        long idDeparture1 = 0;
        long idArrival1 =  2;
        long idDeparture2 = 2;
        long idArrival2 = 4;

        Path p1 = new Path(listSegments1,idDeparture1,idArrival1);
        Path p2 = new Path(listSegments2,idDeparture2,idArrival2);

        ArrayList<Path> listPath = new ArrayList<>();

        listPath.add(p1);
        listPath.add(p2);

        Tour t = new Tour(listPath);
        t.generateRoadMap("/Users/lucastissier/Desktop/Roadmap");

    }

    public void Zoom(ActionEvent event) {
        Gview.zoom();

    }

    public void UnZoom(ActionEvent event) {
        Gview.unZoom();
    }

    public void LoadMap(ActionEvent event) {


        Gview = new GraphicalView(map, overlay, mg); //Creation de la vue graphique à partir de la map et de la zone d'affichage

        //Load the map
        controller.LoadMap(event);

        //Draw the map
        Gview.refreshMap();

        Gview.enableSelection();
        //reactivate Requests button
        btn_load_requests.setDisable(false);
        TextArea.setText("Please load a request list");
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
