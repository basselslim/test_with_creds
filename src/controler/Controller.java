package controler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import model.Intersection;
import model.Map;
import model.Path;
import view.GraphicalView;
import view.TextualView;

import java.io.File;
import java.util.*;

/**
 * 
 */
public class Controller {


    protected ListOfCommand listOfCommand;
    protected State currentState;
    protected Map map;

    protected GraphicalView Gview;
    protected TextualView Tview;

    public void setGview(GraphicalView gview) {
        this.Gview = gview;
    }

    public void setTview(TextualView tview) {
        this.Tview = tview;
    }

    public Map getMap(){
        return map;
    }

    public ListOfCommand getListOfCommand() {
        return listOfCommand;
    }


    protected final InitialState initialState = new InitialState();
    protected final AddPickupState addPickupState = new AddPickupState();
    protected final AddDeliveryState addDeliveryState = new AddDeliveryState();
    protected final ConfirmRequestState confirmRequestState = new ConfirmRequestState();
    protected final DeleteState deleteState = new DeleteState();

    /**
     * Default constructor
     */
    public Controller(Map newMap) {
        listOfCommand = new ListOfCommand();
        currentState = initialState;
        map = newMap;
    }

    protected void setCurrentState(State newState) {
        currentState = newState;
    }

    public void LoadRequests(ActionEvent event) {
        currentState.LoadRequests(event,this,map);
    }

    public void computeTour() {
        Algorithm algo = new Algorithm(map);
        HashMap<Long, HashMap<Long, Path>> mapSmallestPaths = algo.computeSmallestPaths();
        algo.computeOptimalTour(mapSmallestPaths);
    }

    public void ExportRoadMap (ActionEvent event) {
        FileChooser exportFileChooser = new FileChooser();
        exportFileChooser.setTitle("Export RoadMap");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        exportFileChooser.getExtensionFilters().add(extFilter);
        File exportLocation = exportFileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());
        System.out.println(exportLocation);
    }

    public void leftClick(long idIntersection){
        Intersection intersection = map.getListIntersections().get(idIntersection);
        currentState.leftClick(this, map, listOfCommand, intersection);
    }

    public void LoadMap(ActionEvent event) {
        currentState.LoadMap(event, this, map);
    }
    public void mouseOn(long idIntersection) {
        currentState.mouseOn(idIntersection, this);
    }

    public void addDuration(int duration) {
        currentState.addDuration(duration, this);
    }

    public void addRequest() {
        currentState.addRequest(this);
    }

    public void confirmRequest() {
        currentState.confirmRequest(this, map);
    }

    public void undo() {
        currentState.undo(listOfCommand, this);
    }

    public void redo() {
        currentState.redo(listOfCommand,this);
    }



}

