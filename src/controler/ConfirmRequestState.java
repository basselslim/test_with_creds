package controler;

import model.Intersection;
import model.Map;
import model.Request;


public class ConfirmRequestState implements State {

    Request request;
    Intersection PickupPrecedingPoint;
    Intersection DeliveryPrecedingPoint;

    public ConfirmRequestState() {
    }

    @Override
    public void confirmRequest(Controller controller, Map map) {
/**
 *
 */
        if(request != null) {
            long precedingDeliveryId = DeliveryPrecedingPoint.getId();
            long precedingPickupId = PickupPrecedingPoint.getId();
            AddCommand addRequestCommand = new AddCommand(controller. map, request, precedingPickupId, precedingDeliveryId);
            controller.getListOfCommand().add(addRequestCommand);
            controller.setCurrentState(controller.initialState);
            controller.TextMessage.setText("Request added");
            controller.Gview.disableSelection();
        }
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addDeliveryState.reverseAction(controller);
        controller.setCurrentState(controller.addDeliveryState);
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {

    }


    protected void entryAction(Request r, Controller controller) {
        controller.Gview.disableSelection();
        request = new Request(r);
        PickupPrecedingPoint = new Intersection(controller.addDeliveryState.PickupPrecedingPoint);
        DeliveryPrecedingPoint = new Intersection(controller.addDeliveryState.DeliveryPrecedingPoint);
        controller.TextMessage.setText("Confirm adding the request ?");
    }

    protected void reverseAction(Controller controller) {
        controller.Gview.disableSelection();
        controller.TextMessage.setText("Confirm adding the request ?");
    }
}