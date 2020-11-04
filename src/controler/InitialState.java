package controler;

import model.Intersection;
import model.Map;

/**
 * 
 */
public class InitialState implements State {

    /**
     * Default constructor
     */
    public InitialState() {
    }

    @Override
    public void addRequest(Controller controller){
        controller.addPickupState.entryAction(controller);
        controller.setCurrentState(controller.addPickupState);

    }

    @Override
    public void leftclick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i){
        controller.Tview

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
        controller.Gview.disableSelection();
        controller.TextMessage.setText(("Compute Tour, add request or load new request or map."));
    }


}