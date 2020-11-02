package controler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import model.Intersection;
import model.Map;
import model.Path;
import model.Request;

import java.io.File;
import java.util.*;

/**
 * 
 */
public class Controller {

    ListOfCommand listOfCommand;
    State currentState;
    Map map;


    protected final InitialState initialState = new InitialState();
    protected final RequestStatePickUpPoint requestStatePickUpPoint = new RequestStatePickUpPoint();
    protected final RequestStateDeliveryPoint requestStateDeliveryPoint = new RequestStateDeliveryPoint();
    protected final RequestStateConfirmation requestStateConfirmation = new RequestStateConfirmation();
    protected final DeleteState deleteState = new DeleteState();

    /**
     * Default constructor
     */
    public Controller(Map newMap) {
        listOfCommand = new ListOfCommand();
        currentState = initialState;
        map = newMap;
    }

    protected void setCurrentstate(State newState) {
        currentState = newState;
    }

    public void LoadMap(ActionEvent event) {
        //Chargement map
        FileChooser mapFileChooser = new FileChooser();
        mapFileChooser.setTitle("Load Map");
        mapFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File mapFile = mapFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseMapXML(mapFile.getAbsolutePath(), map);

    }

    public void LoadRequests(ActionEvent event) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
    }

    public void computeOptimalTour () {
        Algorithme algo = new Algorithme(map);
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

    protected void setCurrentState(State state){
        currentState = state;
    }

    public void leftClick(long idIntersection){
        Intersection intersection = map.getListIntersections().get(idIntersection);
        currentState.leftClick(this, map, listOfCommand, intersection);
    }

    public void addDuration(int duration) {
        currentState.addDuration(duration);
    }

    public void addRequest(){
        currentState.addRequest(this);
    }




}

