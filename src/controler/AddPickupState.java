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
     * Left click.
     *
     * @param controller
     * @param map
     * @param listOfCommand
     * @param i
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Step step) {
        if (precedingPoint == null) {
            if(map.getRequestByIntersectionId(step.getId()) != null || step.getId() == map.getDepot().getId()) {
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
     * Add duration.
     *
     * @param duration duration to add
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
     * Temporary remove the last added command (this command may be reinserted again with redo).
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.initialState.entryAction(controller);
        controller.Tview.setMessage("Compute Tour, export Roadmap, load a new map or new requests");
        controller.setCurrentState(controller.initialState);

    }

    /**
     * Reinsert the last command removed by undo.
     *
     * @param listOfCommand
     * @param controller
     */
    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.addDeliveryState.request;
        if (controller.addDeliveryState.PickupPrecedingPoint != null && nextReq.getPickUpPoint() != null && nextReq.getPickUpPoint().getPickUpDuration() != 0){
            controller.addDeliveryState.reverseAction(controller);
            controller.setCurrentState(controller.addDeliveryState);
        }
    }

    /**
     *
     * @param controller
     * @param r request
     */
    protected void entryAction(Controller controller, Request r) {
        request = new Request(r);
        precedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the pickup point");

    }

    /**
     *
     * @param controller
     */
    protected void reverseAction(Controller controller) {
        precedingPoint = null;

        controller.Tview.setMessage("Select the preceding point to the pickup point");
    }
}