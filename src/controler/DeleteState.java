package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.Window;
import javax.naming.ldap.Control;


/**
 * State of AGILEPLD when receiving the message delete() from InitialState
 * -> Wait for a leftClick
 * -> Return back to initialState when receiving the message rightClick()
 *
 * @author T-REXANOME
 */
public class DeleteState implements State {

    private Request request;

    /**
     * Left click.
     *
     * @param controler
     * @param map
     * @param listOfCommands
     * @param i
     */
    @Override
    public void leftClick(Controller controler, Map map, ListOfCommand listOfCommands, Intersection i) {

    }

    /**
     *
     * @param controller
     * @param map
     */
    @Override
    public void confirmAction(Controller controller, Map map) {
        Intersection precedingPickup = map.findPrecedingRequestPoint(request.getPickUpPoint());
        Intersection precedingDelivery = map.findPrecedingRequestPoint(request.getDeliveryPoint());
        long precedingPickupId = 0;
        long precedingDeliveryId = 0;
        if(precedingPickup!=null){
            precedingPickupId = precedingPickup.getId();
            precedingDeliveryId = precedingDelivery.getId();
        }
        ReverseCommand deleteRequestCommand = new ReverseCommand(new AddCommand(map,request,precedingPickupId,precedingDeliveryId));
        controller.getListOfCommand().add(deleteRequestCommand);

        controller.initialState.entryAction(controller);
        controller.confirmAction.setVisible(false);
        controller.disableButtons(false);
        controller.Tview.setMessage("Request deleted");
        controller.setCurrentState(controller.initialState);
    }

    /**
     *
     * @param controller
     * @param request
     */
    public void entryAction(Controller controller, Request request) {
        controller.deleteRequest.setDisable(true);
        controller.disableButtons(true);
        controller.confirmAction.setVisible(true);
        this.request = request;
        controller.Tview.setMessage("Confirm deleting the selected Request ?");
    }

}