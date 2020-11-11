package controler;

import model.*;
import model.Map;

/**
 * @author T-REXANOME
 */
public class AddPickupState implements State {

    protected Request request;
    protected Step precedingPoint;

    /**
     * Default constructor.
     */
    public AddPickupState() {
    }

    /**
     * Add the clicked Step as a preceding point to the new pickup point, or as the pickup point itself
     *
     * @param controller
     * @param map           map object
     * @param listOfCommand
     * @param step          request point
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Step step) {
        if (precedingPoint == null) {
            if (map.getRequestByIntersectionId(step.getId()) != null || step.getId() == map.getDepot().getId()) {
                precedingPoint = new Step(step);

                controller.Gview.drawMouseSelection(step);
                controller.Tview.setMessage("Select the pickup point");
            }
        } else {
            PickUpPoint pickup = new PickUpPoint(step, 0);
            request.setPickUpPoint(pickup);

            controller.Gview.drawMouseSelection(step);
            controller.Tview.setMessage("Enter duration");
            controller.addDuration(controller.Tview.durationPopup());

        }
    }

    /**
     * Add the selected intersection as a new Pickup Point to a new request
     *
     * @param controller
     * @param map           map object
     * @param listOfCommand
     * @param i             intersection
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {
        if (precedingPoint != null) {
            if (map.getRequestByIntersectionId(i.getId()) == null || map.getRequestByIntersectionId(i.getId()) != null) {
                PickUpPoint pickup = new PickUpPoint(i, 0);
                request.setPickUpPoint(pickup);

                controller.Gview.drawMouseSelection(i.getId());
                controller.Tview.setMessage("Enter duration");
                controller.addDuration(controller.Tview.durationPopup());
            }
        }
    }

    /**
     * Add a pickup time to the pickup point
     *
     * @param duration   duration to add
     * @param controller
     */
    @Override
    public void addDuration(int duration, Controller controller) {
        request.getPickUpPoint().setPickUpDuration(duration);
        //controller.Gview.disableSelection();

        controller.addDeliveryState.entryAction(controller, request, precedingPoint);
        controller.setCurrentState(controller.addDeliveryState);
    }


    /**
     * Set the entry attributs to the state
     *
     * @param controller
     * @param r          request
     */
    protected void entryAction(Controller controller, Request r) {
        request = new Request(r);
        precedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the pickup point");

    }
}