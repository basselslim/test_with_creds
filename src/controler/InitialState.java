package controler;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import model.Intersection;
import model.Map;
import model.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class InitialState implements State {

    List<Long> CurrentIdList;

    Request request = new Request();

    /**
     * Default constructor
     */
    public InitialState() {
        CurrentIdList = new ArrayList<>();
    }

    @Override
    public void addRequest(Controller controller) {
        //Unselect preceding point
        for (Long id:CurrentIdList) {
            controller.Gview.undrawMouseSelection(id);
        }
        CurrentIdList.clear();

        if(!controller.map.getListRequests().isEmpty()) {
            controller.addPickupState.entryAction(controller, request);
            controller.setCurrentState(controller.addPickupState);
        }
    }

    public void deleteRequest(Controller controller) {
        Request request = controller.map.getRequestByIntersectionId(CurrentIdList.get(0));
        if( request != null) {
            controller.deleteState.entryAction(controller,request);
            controller.setCurrentState(controller.deleteState);

        }
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        //Unselect preceding point
        for (Long id:CurrentIdList) {
            controller.Gview.undrawMouseSelection(id);
        }
        CurrentIdList.clear();


        Request request = controller.map.getRequestByIntersectionId(i.getId());
        if (request != null) {
            //Select both Delivery and Pickup points if the point is a request
            controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
            controller.Gview.drawMouseSelection(request.getDeliveryPoint().getId());
            controller.Tview.selectRequest(i.getId());
            CurrentIdList.add(request.getPickUpPoint().getId());
            CurrentIdList.add(request.getDeliveryPoint().getId());
        }else{
            //Select current point
            controller.Gview.drawMouseSelection(i.getId());
            CurrentIdList.add(i.getId());

        }

    }

    @Override
    public void mouseOn(long idIntersection, Controller controller) {
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
        controller.Gview.enableSelection();
        entryAction(controller);
    }

    public void entryAction(Controller controller) {
        controller.Tview.setMessage("Compute Tour, add request or load new request or map.");
    }


}