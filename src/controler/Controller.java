package controler;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import model.Map;

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
}

