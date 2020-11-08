package controler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import model.Intersection;
import model.Map;
import model.Path;
import view.GraphicalView;
import view.MouseGestures;
import view.TextualView;

import javax.swing.*;
import java.io.File;
import java.util.*;

/**
 * 
 */
public class Controller {
    
    protected MouseGestures mg;
    protected ListOfCommand listOfCommand;
    protected State currentState;
    protected Map map;
    protected Rectangle2D screenBounds;
    protected GraphicalView Gview;
    protected TextualView Tview;

    @FXML
    private Pane overlay;
    @FXML
    private Pane myPane;
    @FXML
    private Button btn_load_requests;
    @FXML
    private javafx.scene.control.TextArea TextArea;
    @FXML
    private Label TextTour;

    protected final InitialState initialState = new InitialState();
    protected final AddPickupState addPickupState = new AddPickupState();
    protected final AddDeliveryState addDeliveryState = new AddDeliveryState();
    protected final ConfirmRequestState confirmRequestState = new ConfirmRequestState();
    protected final DeleteState deleteState = new DeleteState();

    /**
     * Default constructor
     */
    public Controller() {
        screenBounds  = Screen.getPrimary().getBounds();
        mg = new MouseGestures(this);
        map = new Map();
        listOfCommand = new ListOfCommand();
        currentState = initialState;
    }

    //Getters and setters
    public ListOfCommand getListOfCommand() {
        return listOfCommand;
    }

    public Map getMap() {
        return map;
    }

    protected void setCurrentState(State newState) {
        currentState = newState;
    }

    //Public Methods
    public void LoadRequests(ActionEvent event) {
        currentState.LoadRequests(event,this,map);
    }

    public void computeTour(ActionEvent event) {
        Algorithm algo = new Algorithm(map);
        HashMap<Long, HashMap<Long, Path>> mapSmallestPaths = algo.computeSmallestPaths();
        algo.computeOptimalTour(mapSmallestPaths);
        Tview.setTourInfo("Tour length : " + map.getTour().getTourLength());
    }

    public void ExportRoadMap (ActionEvent event) {
        FileChooser exportFileChooser = new FileChooser();
        exportFileChooser.setTitle("Export RoadMap");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        exportFileChooser.getExtensionFilters().add(extFilter);
        File exportLocation = exportFileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());

        map.getTour().generateRoadMap(exportLocation.getPath());
    }

    public void leftClick(long idIntersection){
        Intersection intersection = map.getListIntersections().get(idIntersection);
        currentState.leftClick(this, map, listOfCommand, intersection);
    }

    public void LoadMap(ActionEvent event) {
        Gview = new GraphicalView(map, overlay, mg,screenBounds);
        Tview = new TextualView(map, myPane, TextArea, TextTour);
        map.addObserver(Gview);
        map.addObserver(Tview);
        currentState.LoadMap(event, this, map);
        btn_load_requests.setDisable(false);
    }

    public void Zoom(ActionEvent event) { Gview.zoom(); }

    public void UnZoom(ActionEvent event) {
        Gview.unZoom();
    }

    public void mouseOn(long idIntersection) {
        currentState.mouseOn(idIntersection, this);
    }

    public void addDuration(int duration) {
        currentState.addDuration(duration, this);
    }

    public void addRequest(ActionEvent event) {
        currentState.addRequest(this);
        confirmRequest();
    }

    public void deleteRequest(ActionEvent event){
        currentState.deleteRequest(this);
    }

    public void confirmRequest() {
        currentState.confirmRequest(this, map);
    }

    public void undo(ActionEvent event) {
        currentState.undo(listOfCommand, this);
    }

    public void redo(ActionEvent event) {
        currentState.redo(listOfCommand,this);
    }

}
