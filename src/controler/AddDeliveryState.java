package controler;

import model.*;
import model.Map;

/**
 *
 * @author T-REXANOME
 */
public class AddDeliveryState implements State {

    protected Request request;
    protected Step PickupPrecedingPoint;
    protected Step DeliveryPrecedingPoint;

    /**
     * Default constructor.
     */
    public AddDeliveryState() {
    }

    /**
     * Left click.
     *
     * @param controller
     * @param map
     * @param listOfCommand
     * @param i
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Step step) {
        if (DeliveryPrecedingPoint == null) {
            if (map.getRequestByIntersectionId(step.getId()) != null || step.getId() == map.getDepot().getId()) {
                DeliveryPrecedingPoint = step;

                controller.Gview.drawMouseSelection(step);
                controller.Tview.setMessage("Select the delivery point");
            }
        } else {
            DeliveryPoint delivery = new DeliveryPoint(step, 0);
            request.setDeliveryPoint(delivery);

            controller.Gview.drawMouseSelection(step);
            controller.Tview.setMessage("Enter duration");
            controller.addDuration(controller.Tview.durationPopup());
        }
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {
        if (DeliveryPrecedingPoint != null) {
            if (map.getRequestByIntersectionId(i.getId()) == null || map.getRequestByIntersectionId(i.getId()) != null) {
                DeliveryPoint delivery = new DeliveryPoint(i, 0);
                request.setDeliveryPoint(delivery);

                controller.Gview.drawMouseSelection(i.getId());
                controller.Tview.setMessage("Enter duration");
                controller.addDuration(controller.Tview.durationPopup());
            }
        }
    }

    /**
     * Add a duration.
     *
     * @param duration duration to add
     * @param controller
     */
    @Override
    public void addDuration(int duration, Controller controller) {
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.setCurrentState(controller.confirmRequestState);
        controller.confirmRequestState.entryAction(request, controller);

    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addPickupState.reverseAction(controller);
        unDrawSelections(controller);
        controller.setCurrentState(controller.addPickupState);
    }

    /**
     * Reinsert the last command removed by undo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.confirmRequestState.request;
        unDrawSelections(controller);
        if (controller.confirmRequestState.DeliveryPrecedingPoint != null && nextReq.getDeliveryPoint() != null && nextReq.getDeliveryPoint().getDeliveryDuration() != 0) {
            controller.confirmRequestState.reverseAction(controller);
            controller.setCurrentState(controller.confirmRequestState);
        }
    }

    /**
     *
     * @param controller
     * @param r request
     * @param pickuppredecingPoint
     */
    protected void entryAction(Controller controller, Request r, Step pickuppredecingPoint) {
        request = new Request(r);
        PickupPrecedingPoint = new Step(pickuppredecingPoint);
        DeliveryPrecedingPoint = null;
        controller.Gview.enableSelection();
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

    /**
     *
     * @param controller
     */
    protected void reverseAction(Controller controller) {
        DeliveryPrecedingPoint = null;
        controller.Gview.drawMouseSelection(PickupPrecedingPoint);
        controller.Gview.drawMouseSelection(request.getPickUpPoint().getId());
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

    /**
     *
     * @param controller
     */
    private void unDrawSelections(Controller controller){
        if(DeliveryPrecedingPoint !=null)
            controller.Gview.undrawMouseSelection(DeliveryPrecedingPoint);
        else if(controller.confirmRequestState.DeliveryPrecedingPoint !=null)
            controller.Gview.undrawMouseSelection(controller.confirmRequestState.DeliveryPrecedingPoint);
        if(request.getDeliveryPoint()!=null)
            controller.Gview.undrawMouseSelection(request.getDeliveryPoint().getId());
        controller.Gview.undrawMouseSelection(controller.addPickupState.request.getPickUpPoint().getId());
        controller.Gview.undrawMouseSelection(PickupPrecedingPoint);
    }

}