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
 * Controller
 *
 * @author T-REXANOME
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
    private javafx.scene.control.TextArea TextArea;
    @FXML
    private Label TextTour;
    @FXML
    protected Button confirmAction;
    @FXML
    protected Button LoadMap;
    @FXML
    protected Button LoadRequests;
    @FXML
    protected Button ComputeTour;
    @FXML
    protected Button ExportTour;
    @FXML
    protected Button addRequest;
    @FXML
    protected Button deleteRequest;


    protected final InitialState initialState = new InitialState();
    protected final AddPickupState addPickupState = new AddPickupState();
    protected final AddDeliveryState addDeliveryState = new AddDeliveryState();
    protected final ConfirmRequestState confirmRequestState = new ConfirmRequestState();
    protected final DeleteState deleteState = new DeleteState();

    /**
     * Default constructor.
     */
    public Controller() {

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

    protected void disableButtons(Boolean bool){
        LoadMap.setDisable(bool);
        LoadRequests.setDisable(bool);
        ComputeTour.setDisable(bool);
        ExportTour.setDisable(bool);
    }

    //Public Methods

    /**
     * Load requests.
     *
     * @param event
     */
    public void LoadRequests(ActionEvent event) {
        currentState.LoadRequests(event,this,map);
    }

    /**
     * Compute optimal tour.
     *
     * @param event
     */
    public void computeTour(ActionEvent event) {
        Tview.setMessage("Computing optimal tour...");
        currentState.computeTour(this,map);
    }

    /**
     * Export road map.
     *
     * @param event
     */
    public void ExportRoadMap (ActionEvent event) {
        FileChooser exportFileChooser = new FileChooser();
        exportFileChooser.setTitle("Export RoadMap");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        exportFileChooser.getExtensionFilters().add(extFilter);
        File exportLocation = exportFileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());

        map.getTour().generateRoadMap(exportLocation.getPath());
    }

    /**
     * Left click.
     *
     * @param idIntersection
     */
    public void leftClick(long idIntersection){
        Intersection intersection = map.getListIntersections().get(idIntersection);
        currentState.leftClick(this, map, listOfCommand, intersection);
    }

    /**
     * Load map.
     *
     * @param event
     */
    public void LoadMap(ActionEvent event) {
        Gview = new GraphicalView(map, overlay, mg);
        Tview = new TextualView(map, myPane, TextArea, TextTour, this);
        map.addObserver(Gview);
        map.addObserver(Tview);
        currentState.LoadMap(event, this, map);
        LoadRequests.setDisable(false);
        addRequest.setDisable(true);
        deleteRequest.setDisable(true);
    }

    /**
     * Zoom.
     *
     * @param event
     */
    public void Zoom(ActionEvent event) { Gview.zoom(); }

    /**
     * Unzoom.
     *
     * @param event
     */
    public void UnZoom(ActionEvent event) {
        Gview.unZoom();
    }

    /**
     *
     * @param idIntersection
     */
    public void mouseOn(long idIntersection) {
        currentState.mouseOn(idIntersection, this);
    }

    /**
     * Add duration.
     *
     * @param duration duration to add
     */
    public void addDuration(int duration) {
        currentState.addDuration(duration, this);
    }

    /**
     * Add a request.
     *
     * @param event
     */
    public void addRequest(ActionEvent event) {
        currentState.addRequest(this);
    }

    /**
     * Delete a request.
     *
     * @param event
     */
    public void deleteRequest(ActionEvent event){
        currentState.deleteRequest(this);
    }

    /**
     *
     */
    public void confirmAction() {
        currentState.confirmAction(this, map);
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     *
     * @param event
     */
    public void undo(ActionEvent event) {
        currentState.undo(listOfCommand, this);
    }

    /**
     * Reinsert the last command removed by undo.
     *
     * @param event
     */
    public void redo(ActionEvent event) {
        currentState.redo(listOfCommand,this);
    }
}
