package controler;

import model.*;
import model.Map;

/**
 *
 * @author T-REXANOME
 */
public class AddDeliveryState implements State {

    protected Request request;
    protected Intersection PickupPrecedingPoint;
    protected Intersection DeliveryPrecedingPoint;

    /**
     * Default constructor.
     */
    public AddDeliveryState() {
    }

    /**
     *
     * @return
     */
    public Intersection getpreceding() {
        return DeliveryPrecedingPoint;
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

    /**
     *
     * @param duration duration to add
     * @param controller
     */
    @Override
    public void addDuration(int duration, Controller controller) {
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.Gview.disableSelection();

        controller.confirmRequestState.entryAction(request, controller);
        controller.setCurrentState(controller.confirmRequestState);

    }

    /**
     * Undo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.addPickupState.reverseAction(controller);
        controller.setCurrentState(controller.addPickupState);
    }

    /**
     * Redo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.confirmRequestState.request;
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
    protected void entryAction(Controller controller, Request r, Intersection pickuppredecingPoint) {
        request = new Request(r);
        PickupPrecedingPoint = new Intersection(pickuppredecingPoint);
        DeliveryPrecedingPoint = null;
        controller.Gview.enableSelection();
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }

    /**
     *
     * @param controller
     */
    protected void reverseAction(Controller controller) {
        controller.Gview.enableSelection();
        DeliveryPrecedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the delivery point");
    }
}