package controler;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.*;
import model.Map;

import java.io.File;
import java.util.*;

/**
 *
 */
public class InitialState implements State {

    List<Long> CurrentIdList;
    List<Step> CurrentStepList;

    Request request = new Request();
    Boolean isTourComputed = false;

    /**
     * Default constructor
     */
    public InitialState() {
        CurrentIdList = new ArrayList<>();
        CurrentStepList = new ArrayList<>();
    }

    @Override
    public void addRequest(Controller controller) {

        controller.deleteRequest.setDisable(true);
        controller.addRequest.setDisable(true);
        unSelectPoints(controller);

        if(!controller.map.getListRequests().isEmpty()) {
            controller.disableButtons(true);
            controller.addPickupState.entryAction(controller, request);
            controller.setCurrentState(controller.addPickupState);
        }
    }

    public void deleteRequest(Controller controller) {
        Request request = CurrentStepList.get(0).getRequest();
        if( request != null) {
            controller.addRequest.setDisable(true);
            controller.setCurrentState(controller.deleteState);
            controller.deleteState.entryAction(controller,request);

        }
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {
        unSelectPoints(controller);
        unSelectSteps(controller);

        //Select current point
        controller.Gview.drawMouseSelection(i.getId());
        CurrentIdList.add(i.getId());
        controller.deleteRequest.setDisable(true);

    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Step step) {
        unSelectPoints(controller);
        unSelectSteps(controller);

        Request request = step.getRequest();
        //Select both Delivery and Pickup points if the point is a request
        controller.Gview.drawMouseSelection(request.getPickUpPoint());
        controller.Gview.drawMouseSelection(request.getDeliveryPoint());
        controller.Tview.selectRequest(request,false);
        CurrentStepList.add(request.getPickUpPoint());
        CurrentStepList.add(request.getDeliveryPoint());
        controller.deleteRequest.setDisable(false);

    }



    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.undo();
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.redo();
    }

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

    @Override
    public void LoadRequests(ActionEvent event, Controller controller, Map map) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
        controller.addRequest.setDisable(true);
        controller.Tview.setMessage("Compute Tour, add request or load new request or map.");
        controller.Gview.enableSelection();
    }

    @Override
    public void computeTour(Controller controller, Map map){
        unSelectPoints(controller);
        isTourComputed = true;
        Algorithm algo = new Algorithm(map);
        HashMap<Long, HashMap<Long, Path>> mapSmallestPaths = algo.computeSmallestPaths();
        algo.computeOptimalTour(mapSmallestPaths);
        controller.Tview.setMessage("Optimal tour computed !");
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

    private void unSelectSteps(Controller controller){
        for (Step step:CurrentStepList) {
            controller.Gview.undrawMouseSelection(step);
        }
        CurrentStepList.clear();
    }


    public void entryAction(Controller controller) {
        if (isTourComputed)
            controller.addRequest.setDisable(false);
        controller.disableButtons(false);
        CurrentIdList.clear();
    }


}