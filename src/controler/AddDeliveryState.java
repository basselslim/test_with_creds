package controler;

import model.*;
import model.Map;

/**
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
     * Add the clicked Step as a preceding point to the new delivery point, or as the delivery point itself.
     *
     * @param controller    controller
     * @param map           map object
     * @param listOfCommand list of commands
     * @param step          request point
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

    /**
     * Add the selected intersection as a new Delivery Point to a new request.
     *
     * @param controller    controller
     * @param map           map object
     * @param listOfCommand list of commands
     * @param i             intersection
     */
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
     * Add a delivery time to the delivery point.
     *
     * @param duration   duration to add
     * @param controller controller
     */
    @Override
    public void addDuration(int duration, Controller controller) {
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.setCurrentState(controller.confirmRequestState);
        controller.confirmRequestState.entryAction(request, controller);

    }

    /**
     * Set the entry attributes to the state.
     *
     * @param controller           controller
     * @param r                    request
     * @param pickuppredecingPoint preceding pick up point
     */
    protected void entryAction(Controller controller, Request r, Step pickuppredecingPoint) {
        request = new Request(r);
        PickupPrecedingPoint = new Step(pickuppredecingPoint);
        DeliveryPrecedingPoint = null;
        controller.Gview.enableSelection();
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }


}