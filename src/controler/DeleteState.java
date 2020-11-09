package controler;

import model.Intersection;
import model.Map;
import model.Request;
import model.Step;
import view.Window;

import javax.naming.ldap.Control;

public class DeleteState implements State {
    private Request request;
    // State of AGILEPLD when receiving the message delete() from InitialState
    // -> Wait for a leftClick
    // -> Return back to initialState when receiving the message rightClick()

    @Override
    public void leftClick(Controller controler, Map map, ListOfCommand listOfCommands, Intersection i) {

    }

    @Override
    public void confirmAction(Controller controller, Map map) {
        Step precedingPickup = map.findPrecedingRequestPoint(request.getPickUpPoint());
        Step precedingDelivery = map.findPrecedingRequestPoint(request.getDeliveryPoint());

        ReverseCommand deleteRequestCommand = new ReverseCommand(new AddCommand(map,request,precedingPickup,precedingDelivery));
        controller.getListOfCommand().add(deleteRequestCommand);

        controller.initialState.entryAction(controller);
        controller.confirmAction.setVisible(false);
        controller.disableButtons(false);
        controller.Tview.setMessage("Request deleted");
        controller.setCurrentState(controller.initialState);
    }

    public void entryAction(Controller controller, Request request) {
        controller.deleteRequest.setDisable(true);
        controller.disableButtons(true);
        controller.confirmAction.setVisible(true);
        this.request = request;
        controller.Tview.setMessage("Confirm deleting the selected Request ?");
    }

}