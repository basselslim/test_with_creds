package controler;

import model.*;
import model.Map;

/**
 *
 */
public class AddDeliveryState implements State {

    protected Request request;
    protected Intersection PickupPrecedingPoint;
    protected Intersection DeliveryPrecedingPoint;

    /**
     * Default constructor
     */

    public AddDeliveryState() {
    }


    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        if (DeliveryPrecedingPoint == null) {
            if (map.getRequestByIntersectionId(i.getId()) != null) {
                DeliveryPrecedingPoint = i;
                controller.Tview.setMessage("Select the delivery point");
            }
        } else {
            if (map.getRequestByIntersectionId(i.getId()) == null) {
                DeliveryPoint delivery = new DeliveryPoint(i, 0);
                request.setDeliveryPoint(delivery);

                controller.Tview.setMessage("Enter duration");
                controller.addDuration(controller.Tview.durationPopup());
            }

        }
    }

    @Override
    public void addDuration(int duration, Controller controller) {
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.Gview.disableSelection();

        controller.confirmRequestState.entryAction(request, controller);
        controller.setCurrentState(controller.confirmRequestState);

    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addPickupState.reverseAction(controller);
        controller.setCurrentState(controller.addPickupState);
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.confirmRequestState.request;
        if (controller.confirmRequestState.DeliveryPrecedingPoint != null && nextReq.getDeliveryPoint() != null && nextReq.getDeliveryPoint().getDeliveryDuration() != 0) {
            controller.confirmRequestState.reverseAction(controller);
            controller.setCurrentState(controller.confirmRequestState);
        }
    }

    protected void entryAction(Controller controller, Request r, Intersection pickuppredecingPoint) {
        request = new Request(r);
        PickupPrecedingPoint = new Intersection(pickuppredecingPoint);
        DeliveryPrecedingPoint = null;
        controller.Gview.enableSelection();
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

    protected void reverseAction(Controller controller) {
        controller.Gview.enableSelection();
        DeliveryPrecedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

}