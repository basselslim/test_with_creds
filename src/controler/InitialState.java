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

/**
 *
 */
public class InitialState implements State {

    long CurrentId;

    Request request = new Request();

    /**
     * Default constructor
     */
    public InitialState() {
    }

    @Override
    public void addRequest(Controller controller) {
        controller.addPickupState.entryAction(controller, request);
        controller.setCurrentState(controller.addPickupState);
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        //Unselect preceding point
        Circle circle = controller.Gview.circles.get(CurrentId);
        Rectangle rectangle = controller.Gview.rectangles.get(CurrentId);
        if(circle!=null)
            controller.Gview.undrawMouseSelection(circle);
        if(rectangle!=null)
            controller.Gview.undrawMouseSelection(rectangle);

        //Select preceding point
        circle = controller.Gview.circles.get(i.getId());
        rectangle = controller.Gview.rectangles.get(i.getId());
        if(circle!=null)
            controller.Gview.drawMouseSelection(circle);
        if(rectangle!=null)
            controller.Gview.drawMouseSelection(rectangle);

        CurrentId = i.getId();

        //Diplay in textual view if the point is a request
        if (controller.map.getRequestByIntersectionId(i.getId()) != null)
            controller.Tview.selectRequest(i.getId());
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
    }

    @Override
    public void LoadRequests(ActionEvent event, Controller controller, Map map) {
        FileChooser requestsFileChooser = new FileChooser();
        requestsFileChooser.setTitle("Load Requests");
        requestsFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File requestsFile = requestsFileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        XMLLoader xmlloader = new XMLLoader();
        xmlloader.parseRequestXML(requestsFile.getAbsolutePath(), map);
    }

    public void entryAction(Controller controller) {
        controller.Tview.setMessage("Compute Tour, add request or load new request or map.");
    }


}