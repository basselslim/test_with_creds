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
            if (map.getRequestByIntersectionId(i.getId()) != null || i.getId() == map.getDepot().getId()) {
                DeliveryPrecedingPoint = i;

                controller.Gview.drawMouseSelection(i.getId());
                controller.Tview.setMessage("Select the delivery point");
            }
        } else {
            if (map.getRequestByIntersectionId(i.getId()) == null) {
                DeliveryPoint delivery = new DeliveryPoint(i, 0);
                request.setDeliveryPoint(delivery);

                controller.Gview.drawMouseSelection(i.getId());
                controller.Tview.setMessage("Enter duration");
                controller.addDuration(controller.Tview.durationPopup());
            }
        }
    }

    @Override
    public void addDuration(int duration, Controller controller) {
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.confirmRequestState.entryAction(request, controller);
        controller.setCurrentState(controller.confirmRequestState);

    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addPickupState.reverseAction(controller);
        unDrawSelections(controller);
        controller.setCurrentState(controller.addPickupState);
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.confirmRequestState.request;
        unDrawSelections(controller);
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
        DeliveryPrecedingPoint = null;
        controller.Gview.drawMouseSelection(PickupPrecedingPoint.getId());
        controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

    private void unDrawSelections(Controller controller){
        if(DeliveryPrecedingPoint !=null)
            controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint.getId());
        else if(controller.confirmRequestState.DeliveryPrecedingPoint !=null)
            controller.Gview.undrawMouseSelection(controller.confirmRequestState.DeliveryPrecedingPoint.getId());
        if(request.getDeliveryPoint()!=null)
            controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.undrawMouseSelection(controller.addPickupState.request.getPickUpPoint().getId());
        controller.Gview.undrawMouseSelection(PickupPrecedingPoint.getId());
    }

}