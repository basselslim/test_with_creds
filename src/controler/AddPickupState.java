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
            if(map.getRequestByTourStopId(i.getId()) == null) {
                precedingPoint = new Intersection(i);
                controller.TextMessage.setText("Select the pickup point");
            }
        } else {
            if(map.getRequestByTourStopId(i.getId()) != null) {
                PickUpPoint pickup = new PickUpPoint(i, 0);
                request.setPickUpPoint(pickup);

                controller.TextMessage.setText(("Enter duration"));
                controller.addDuration(controller.Tview.durationPopup());
            }

        }
    }

    @Override
    public void addDuration(int duration, Controller controller) {
        request.getPickUpPoint().setPickUpDuration(duration);
        //controller.Gview.disableSelection();

        controller.addDeliveryState.entryAction(controller, request);
        controller.setCurrentState(controller.addDeliveryState);
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.initialState.entryAction(controller);
        controller.setCurrentState(controller.initialState);

    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        if( precedingPoint != null && request.getPickUpPoint() != null && request.getPickUpPoint().getPickUpDuration() != 0)
            controller.setCurrentState(controller.addDeliveryState);

    }

    protected void entryAction(Controller controller) {
        controller.Gview.enableSelection();
        request = new Request();
        precedingPoint = null;
        controller.TextMessage.setText("Select the preceding point to the pickup point");
    }
}