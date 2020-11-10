package controler;

import model.Intersection;
import model.Map;
import model.Request;
import model.Step;

import javax.naming.ldap.Control;

/**
 * Confirm request state.
 *
 * @author T-REXANOME
 */
public class ConfirmRequestState implements State {

    protected Request request;
    protected Step PickupPrecedingPoint;
    protected Step DeliveryPrecedingPoint;

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
    public void confirmAction(Controller controller, Map map) {
        if(request != null) {
            
            AddCommand addRequestCommand = new AddCommand(controller. map, request, PickupPrecedingPoint, DeliveryPrecedingPoint);
            int errorCode = controller.getListOfCommand().add(addRequestCommand);

            controller.initialState.entryAction(controller);
            controller.setCurrentState(controller.initialState);
            unDrawSelection(controller);

            controller.confirmAction.setVisible(false);
            controller.addRequest.setDisable(false);
            controller.Gview.disableSelection();


                controller.Tview.setMessage("Request added");
            if (errorCode == 1) {
                controller.Tview.setMessage("Error : Can't find a path to the new pick up point.");
            }else if (errorCode == 2) {
                controller.Tview.setMessage("Error : Can't find a path to the new delivery point.");
            }
        }
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addDeliveryState.reverseAction(controller);
        controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint);
        controller.Gview.undrawMouseSelection(request.getDeliveryPoint());
        controller.setCurrentState(controller.addDeliveryState);
        controller.confirmAction.setVisible(false);
    }

    /**
     * Reinsert the last command removed by undo.
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
        controller.confirmAction.setVisible(true);
        request = new Request(r);
        PickupPrecedingPoint = new Step(controller.addDeliveryState.PickupPrecedingPoint);
        DeliveryPrecedingPoint = new Step(controller.addDeliveryState.DeliveryPrecedingPoint);
        controller.confirmAction();
    }

    /**
     * Reverse action.
     *
     * @param controller
     */
    protected void reverseAction(Controller controller) {
        controller.addRequest.setDisable(true);
        controller.deleteRequest.setDisable(true);
        controller.confirmAction.setVisible(true);
        controller.Gview.disableSelection();
        drawSelection(controller);
        controller.Tview.setMessage("Confirm adding the request ?");
    }

    /**
     *
     * @param controller
     */
    private void drawSelection(Controller controller){
        controller.Gview.drawMouseSelection(DeliveryPrecedingPoint);
        controller.Gview.drawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.drawMouseSelection(PickupPrecedingPoint);
        controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
    }

    /**
     *
     * @param controller
     */
    private void unDrawSelection(Controller controller){
        controller.Gview.undrawMouseSelection(request.getPickUpPoint().getId());
        controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint);
        controller.Gview.undrawMouseSelection(PickupPrecedingPoint);
    }
}