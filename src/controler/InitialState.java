package controler;

import model.Intersection;
import model.Map;
import model.Request;

/**
 * 
 */
public class InitialState implements State {

    Request request = new Request();
    /**
     * Default constructor
     */
    public InitialState() {
    }

    @Override
    public void addRequest(Controller controller){
        controller.addPickupState.entryAction(controller, request);
        controller.setCurrentState(controller.addPickupState);

    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i){

    }

    @Override
    public void mouseOn(long idIntersection, Controller controller){
        controller.Tview.selectRequest(idIntersection);
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.undo();
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        listOfCommand.redo();
    }



    public void entryAction(Controller controller) {
        controller.TextMessage.setText(("Compute Tour, add request or load new request or map."));
    }


}