package controler;

import model.Intersection;
import model.Map;
import model.Request;

import javax.naming.ldap.Control;


public class ConfirmRequestState implements State {

    protected Request request;
    protected Intersection PickupPrecedingPoint;
    protected Intersection DeliveryPrecedingPoint;

    public ConfirmRequestState() {
    }

    @Override
    public void confirmAction(Controller controller, Map map) {
/**
 *
 */
        if(request != null) {
            long precedingDeliveryId = DeliveryPrecedingPoint.getId();
            long precedingPickupId = PickupPrecedingPoint.getId();
            AddCommand addRequestCommand = new AddCommand(controller. map, request, precedingPickupId, precedingDeliveryId);
            controller.getListOfCommand().add(addRequestCommand);

            controller.initialState.entryAction(controller);
            controller.setCurrentState(controller.initialState);
            unDrawSelection(controller);

            controller.Tview.setMessage("Request added");
            controller.confirmAction.setVisible(false);
            controller.addRequest.setDisable(false);
            controller.Gview.disableSelection();
        }
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addDeliveryState.reverseAction(controller);
        controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint.getId());
        controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.setCurrentState(controller.addDeliveryState);
        controller.confirmAction.setVisible(false);
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {

    }


    protected void entryAction(Request r, Controller controller) {
        controller.confirmAction.setVisible(true);
        request = new Request(r);
        PickupPrecedingPoint = new Intersection(controller.addDeliveryState.PickupPrecedingPoint);
        DeliveryPrecedingPoint = new Intersection(controller.addDeliveryState.DeliveryPrecedingPoint);
        controller.Tview.setMessage("Confirm adding the request ?");
    }

    protected void reverseAction(Controller controller) {
        controller.addRequest.setDisable(true);
        controller.deleteRequest.setDisable(true);
        controller.confirmAction.setVisible(true);
        controller.Gview.disableSelection();
        drawSelection(controller);
        controller.Tview.setMessage("Confirm adding the request ?");
    }

    private void drawSelection(Controller controller){
        controller.Gview.drawMouseSelection(DeliveryPrecedingPoint.getId());
        controller.Gview.drawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.drawMouseSelection(PickupPrecedingPoint.getId());
        controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
    }

    private void unDrawSelection(Controller controller){
        controller.Gview.undrawMouseSelection(request.getPickUpPoint().getId());
        controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint.getId());
        controller.Gview.undrawMouseSelection(PickupPrecedingPoint.getId());
    }


}