package controler;

import model.*;
import model.Map;

/**
 * 
 */
public class AddDeliveryState implements State {

    private Request request;
    private Intersection precedingPoint;

    /**
     * Default constructor
     */


    public AddDeliveryState() {
    }

    public Intersection getpreceding(){
        return precedingPoint;
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        if (precedingPoint == null) {
            precedingPoint = i;
            controller.TextMessage.setText("Select the delivery point");
        } else {
            DeliveryPoint delivery = new DeliveryPoint(i,0);
            request.setDeliveryPoint(delivery);

            controller.TextMessage.setText(("Enter duration"));
            controller.addDuration(controller.Tview.durationPopup());

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
        controller.setCurrentState(controller.addPickupState);
        controller.Gview.refreshMap();
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {
        if(precedingPoint != null && request.getDeliveryPoint() != null && request.getDeliveryPoint().getDeliveryDuration() != 0)
        controller.setCurrentState(controller.confirmRequestState);
    }

    protected void entryAction(Controller controller, Request r) {
        request = new Request(r);
        controller.Gview.enableSelection();
        controller.TextMessage.setText(("Select the preceding point to the delivery point"));
    }

}