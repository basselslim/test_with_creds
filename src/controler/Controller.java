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
import model.*;
import model.Map;
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
    protected Pane overlay;
    @FXML
    protected Pane myPane;
    @FXML
    protected javafx.scene.control.TextArea TextArea;
    @FXML
    protected Label TextTour;
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
    @FXML
    protected Button undo;
    @FXML
    protected Button redo;

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

    protected void disableButtons(Boolean bool) {
        LoadMap.setDisable(bool);
        LoadRequests.setDisable(bool);
        ComputeTour.setDisable(bool);
        ExportTour.setDisable(bool);
        undo.setDisable(bool);
        redo.setDisable(bool);
    }

    //Public Methods

    /**
     * Load requests.
     *
     * @param event event
     */
    public void LoadRequests(ActionEvent event) {
        currentState.LoadRequests(event, this, map);
    }

    /**
     * Compute tour.
     *
     * @param event event
     */
    public void computeTour(ActionEvent event) {
        Tview.setMessage("Computing optimal tour...");
        currentState.computeTour(this, map);
    }

    /**
     * Export road map.
     *
     * @param event event
     */
    public void ExportRoadMap(ActionEvent event) {
        FileChooser exportFileChooser = new FileChooser();
        exportFileChooser.setTitle("Export RoadMap");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        exportFileChooser.getExtensionFilters().add(extFilter);
        File exportLocation = exportFileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (exportLocation != null) {
            map.getDeliveryTour().generateRoadMap(exportLocation.getPath());
        }
    }

    /**
     * Select an Intersection by Id.
     *
     * @param idIntersection id of the intersection to select
     */
    public void leftClick(long idIntersection) {
        Intersection intersection = map.getListIntersections().get(idIntersection);
        currentState.leftClick(this, map, listOfCommand, intersection);
    }

    /**
     * Select a request by one of its steps.
     *
     * @param step step to select
     */
    public void leftClick(Step step) {
        currentState.leftClick(this, map, listOfCommand, step);
    }

    /**
     * Load map.
     *
     * @param event event
     */
    public void LoadMap(ActionEvent event) {

        currentState.LoadMap(event, this, map);

    }

    /**
     * Zoom into the map.
     *
     * @param event event
     */
    public void Zoom(ActionEvent event) {
        Gview.zoom();
    }

    /**
     * Zoom out.
     *
     * @param event event
     */
    public void UnZoom(ActionEvent event) {
        Gview.unZoom();
    }

    public void resetView(ActionEvent event) {
        Gview.resetView();
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
     * Enter in the add request state.
     *
     * @param event event
     */
    public void addRequest(ActionEvent event) {
        currentState.addRequest(this);
    }

    /**
     * Enter in the delete request state.
     *
     * @param event event
     */
    public void deleteRequest(ActionEvent event) {
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
     * @param event event
     */
    public void undo(ActionEvent event) {
        currentState.undo(listOfCommand, this);
    }

    /**
     * Reinsert the last command removed by undo.
     *
     * @param event event
     */
    public void redo(ActionEvent event) {
        currentState.redo(listOfCommand, this);
    }
}
