package view;

import controler.Algorithm;
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

        double length1 = 3000;
        String streetName1 = "Rue de Rivoli";
        long destination1 = 1;
        Segment s1 = new Segment(length1,streetName1,destination1);

        double length2 = 200;
        String streetName2 = "Rue de Saxe";
        long destination2 = 2;
        Segment s2 = new Segment(length2,streetName2,destination2);

        double length3 = 1300;
        String streetName3 = "Rue du T-rexanome";
        long destination3 = 3;
        Segment s3 = new Segment(length3,streetName3,destination3);

        double length4 = 450;
        String streetName4 = "Rue du j'ai plus d'inspi";
        long destination4 = 4;
        Segment s4 = new Segment(length4,streetName4,destination4);

        ArrayList<Segment> listSegments0 = new ArrayList<>();
        ArrayList<Segment> listSegments1 = new ArrayList<>();
        ArrayList<Segment> listSegments2 = new ArrayList<>();

        listSegments0.add(s1);
        listSegments1.add(s2);
        listSegments2.add(s3);
        listSegments2.add(s4);

        long idDeparture1 = 0;
        long idArrival1 =  2;
        long idDeparture2 = 2;
        long idArrival2 = 4;

        Path p0 = new Path(listSegments0,idDeparture1,1);
        Path p1 = new Path(listSegments1,1,idArrival1);
        Path p2 = new Path(listSegments2,idDeparture2,idArrival2);

        ArrayList<Path> listPath = new ArrayList<>();

        listPath.add(p0);
        listPath.add(p1);
        listPath.add(p2);


        PickUpPoint i1 = new PickUpPoint(1,0.0,0.0,10);
        PickUpPoint i2 = new PickUpPoint(2,0.0,0.0,21);
        DeliveryPoint i4 = new DeliveryPoint(4,0.0,0.0,32);
        Intersection i0 = new Intersection(0,0.0,0.0);
        Depot d = new Depot(0,"09:00");

        Request r1 = new Request(i1,i4);
        Request r2 = new Request(i2,i4);
        ArrayList<Request> listRequest = new ArrayList<Request>();
        listRequest.add(r1);
        listRequest.add(r2);

        HashMap<Long, Intersection> listIntersection = new HashMap<Long, Intersection>();
        listIntersection.put(0l,i0);
        listIntersection.put(1l,i1);
        listIntersection.put(2l,i2);
        listIntersection.put(3l,i4);
        listIntersection.put(4l,i4);


        Map map = new Map(listIntersection);
        map.setListRequest(listRequest);
        map.setDepot(d);

        Tour t = new Tour(map,listPath);
        t.groupRequestIntersections();
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
        map.addObserver(Gview);

        //Load the map
        controller.LoadMap(event);

        controller.setTextArea(TextArea);
        controller.setGview(Gview);

        //Draw the map
        Gview.refreshMap();



        //reactivate Requests button
        btn_load_requests.setDisable(false);
        TextArea.setText("Please load a request list");
    }

    public void LoadRequests(ActionEvent event) {
        Tview = new TextualView(map, myPane);
        map.addObserver(Tview);
        controller.LoadRequests(event);
    }

    public void addRequest(ActionEvent event) {
        controller.addRequest();
        controller.confirmRequest(); //TEMPORAIRE

    }

    public void Compute(ActionEvent event) {
        controller.computeOptimalTour();
    }

    public void Export(ActionEvent event) {
        TextInputDialog popup = new TextInputDialog();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
        popup.setTitle("Duration");
        popup.setHeaderText("");
        popup.setContentText("Please enter the duration:");
        Optional<String> result = popup.showAndWait();
        controller.addDuration(Integer.valueOf(result.get()));

    }

}
