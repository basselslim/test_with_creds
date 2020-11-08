package controler;

import model.Intersection;
import model.Map;
import model.Request;

/**
 * @authorT-REXANOME
 */
public class ConfirmRequestState implements State {

    protected Request request;
    protected Intersection PickupPrecedingPoint;
    protected Intersection DeliveryPrecedingPoint;

    /**
     * Default Constructor.
     */
    public ConfirmRequestState() {
    }

    /**
     * Constructor.
     *
     * @param controller
     * @param map
     */
    @Override
    public void confirmRequest(Controller controller, Map map) {
        if (request != null) {
            long precedingDeliveryId = DeliveryPrecedingPoint.getId();
            long precedingPickupId = PickupPrecedingPoint.getId();
            AddCommand addRequestCommand = new AddCommand(controller.map, request, precedingPickupId, precedingDeliveryId);
            controller.getListOfCommand().add(addRequestCommand);
            controller.setCurrentState(controller.initialState);
            controller.Tview.setMessage("Request added");
            controller.Gview.undrawMouseSelection(request.getPickUpPoint().getId());
            controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
            controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint.getId());
            controller.Gview.undrawMouseSelection(PickupPrecedingPoint.getId());

            controller.Gview.disableSelection();
        }
    }

    /**
     * Undo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addDeliveryState.reverseAction(controller);
        controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint.getId());
        controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.setCurrentState(controller.addDeliveryState);
    }

    /**
     * Redo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {

    }

    /**
     * @param r          request
     * @param controller
     */
    protected void entryAction(Request r, Controller controller) {
        request = new Request(r);
        PickupPrecedingPoint = new Intersection(controller.addDeliveryState.PickupPrecedingPoint);
        DeliveryPrecedingPoint = new Intersection(controller.addDeliveryState.DeliveryPrecedingPoint);
        controller.Tview.setMessage("Confirm adding the request ?");
    }

    /**
     * Reverse action.
     *
     * @param controller
     */
    protected void reverseAction(Controller controller) {
        controller.Gview.disableSelection();
        controller.Gview.drawMouseSelection(DeliveryPrecedingPoint.getId());
        controller.Gview.drawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.drawMouseSelection(PickupPrecedingPoint.getId());
        controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
        controller.Tview.setMessage("Confirm adding the request ?");
    }
}