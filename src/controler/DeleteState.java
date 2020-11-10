package controler;

import model.Intersection;
import model.Map;
import model.Request;
import model.Step;
import view.Window;

import javax.naming.ldap.Control;


/**
 * State of AGILEPLD when receiving the message delete() from InitialState
 * - Wait for a leftClick
 * - Return back to initialState when receiving the message rightClick()
 *
 * @author T-REXANOME
 */
public class DeleteState implements State {

    private Request request;

    /**
     * Left click.
     *
     * @param controler
     * @param map            map object
     * @param listOfCommands
     * @param i              intersection
     */
    @Override
    public void leftClick(Controller controler, Map map, ListOfCommand listOfCommands, Intersection i) {

    }

    /**
     * @param controller
     * @param map        map object
     */
    @Override
    public void confirmAction(Controller controller, Map map) {
        Step precedingPickup = map.findPrecedingRequestPoint(request.getPickUpPoint());
        Step precedingDelivery = map.findPrecedingRequestPoint(request.getDeliveryPoint());

        ReverseCommand deleteRequestCommand = new ReverseCommand(new AddCommand(map, request, precedingPickup, precedingDelivery));
        controller.getListOfCommand().add(deleteRequestCommand);

        controller.initialState.entryAction(controller);
        controller.disableButtons(false);
        controller.Tview.setMessage("Request deleted");
        controller.setCurrentState(controller.initialState);
    }

    /**
     * @param controller
     * @param request
     */
    public void entryAction(Controller controller, Request request) {
        controller.deleteRequest.setDisable(true);
        controller.disableButtons(true);
        this.request = request;
        controller.confirmAction();
    }

}