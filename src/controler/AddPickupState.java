package controler;

import model.*;
import model.Map;

/**
 *
 */
public class AddPickupState implements State {

    private Request request;
    private Intersection precedingPoint;

    public AddPickupState() {
    }

    public Intersection getpreceding(){
        return precedingPoint;
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        if (precedingPoint == null) {
            if(map.getRequestByTourStopId(i.getId()) != null) {
                precedingPoint = new Intersection(i);
                controller.Tview.setMessage("Select the pickup point");
            }
        } else {
            if(map.getRequestByTourStopId(i.getId()) == null) {
                PickUpPoint pickup = new PickUpPoint(i, 0);
                request.setPickUpPoint(pickup);

                controller.Tview.setMessage("Enter duration");
                controller.addDuration(controller.Tview.durationPopup());
            }

        }
    }

    @Override
    public void addDuration(int duration, Controller controller) {
        request.getPickUpPoint().setPickUpDuration(duration);
        //controller.Gview.disableSelection();

        controller.addDeliveryState.entryAction(controller, request, precedingPoint);
        controller.setCurrentState(controller.addDeliveryState);
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.initialState.entryAction(controller);
        controller.setCurrentState(controller.initialState);

    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        Request nextReq = controller.addDeliveryState.request;
        if (controller.addDeliveryState.PickupPrecedingPoint != null && nextReq.getPickUpPoint() != null && nextReq.getPickUpPoint().getPickUpDuration() != 0){
            controller.addDeliveryState.reverseAction(controller);
            controller.setCurrentState(controller.addDeliveryState);
    }

    }

    protected void entryAction(Controller controller, Request r) {
        request = new Request(r);
        controller.Gview.enableSelection();
        precedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the pickup point");

    }

    protected void reverseAction(Controller controller) {
        controller.Gview.enableSelection();
        precedingPoint = null;
        controller.Tview.setMessage("Select the preceding point to the pickup point");
    }
}