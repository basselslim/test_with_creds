package controler;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Intersection;
import model.Map;
import model.Path;
import model.Request;

import java.io.File;
import java.util.*;

/**
 * @author T-REXANOME
 */
public class InitialState implements State {

    List<Long> CurrentIdList;

    Request request = new Request();

    /**
     * Default constructor.
     */
    public InitialState() {
        CurrentIdList = new ArrayList<>();
    }

    /**
     * Add a request.
     *
     * @param controller
     */
    @Override
    public void addRequest(Controller controller) {

        unSelectPoints(controller);

        if(!controller.map.getListRequests().isEmpty()) {
            controller.disableButtons(true);
            controller.addPickupState.entryAction(controller, request);
            controller.setCurrentState(controller.addPickupState);
        }
    }

    /**
     * Delete a request.
     *
     * @param controller
     */
    public void deleteRequest(Controller controller) {
        Request request = controller.map.getRequestByIntersectionId(CurrentIdList.get(0));
        if( request != null) {
            controller.deleteState.entryAction(controller,request);
            controller.setCurrentState(controller.deleteState);

        }
    }

    /**
     * Left click.
     *
     * @param controller
     * @param map
     * @param listOfCommand
     * @param i             intersection
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        unSelectPoints(controller);

        Request request = controller.map.getRequestByIntersectionId(i.getId());
        if (request != null) {
            //Select both Delivery and Pickup points if the point is a request
            controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
            controller.Gview.drawMouseSelection(request.getDeliveryPoint().getId());
            controller.Tview.selectRequest(map.getRequestByIntersectionId(i.getId()),false);
            CurrentIdList.add(request.getPickUpPoint().getId());
            CurrentIdList.add(request.getDeliveryPoint().getId());
        }else{
            //Select current point
            controller.Gview.drawMouseSelection(i.getId());
            CurrentIdList.add(i.getId());


        }

    }

    /**
     * @param idIntersection id of an interection
     * @param controller
     */
    @Override
    public void mouseOn(long idIntersection, Controller controller) {
    }

    /**
     * Undo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.undo();
    }

    /**
     * Redo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.redo();
    }

    /**
     * Load the map.
     *
     * @param event
     * @param controller
     * @param map
     */
    @Override
    public void LoadMap(ActionEvent event, Controller controller, Map map) {
        FileChooser mapFileChooser = new FileChooser();
        mapFileChooser.setTitle("Load Map");
        mapFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File mapFile = mapFileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseMapXML(mapFile.getAbsolutePath(), map);

        controller.Tview.setMessage("Please load a request list");
    }

    /**
     * Load requests.
     *
     * @param event
     * @param controller
     * @param map
     */
    @Override
    public void LoadRequests(ActionEvent event, Controller controller, Map map) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
        controller.Tview.setMessage("Compute Tour, add request or load new request or map.");
        controller.Gview.enableSelection();
    }

    /**
     * Compute optimal tour.
     *
     * @param controller
     * @param map
     */
    @Override
    public void computeTour(Controller controller, Map map){
        unSelectPoints(controller);

        Algorithm algo = new Algorithm(map);
        HashMap<Long, HashMap<Long, Path>> mapSmallestPaths = algo.computeSmallestPaths();
        algo.computeOptimalTour(mapSmallestPaths);
        controller.addRequest.setDisable(false);
        controller.Tview.setTourInfo("Tour length : " + map.getTour().getTourLength());
    }

    /**
     * Unselects all points
     */
    private void unSelectPoints(Controller controller){
        for (Long id:CurrentIdList) {
            controller.Gview.undrawMouseSelection(id);
        }
        CurrentIdList.clear();
    }

    /**
     *
     * @param controller
     */
    public void entryAction(Controller controller) {
        controller.disableButtons(false);
        CurrentIdList.clear();
    }
}