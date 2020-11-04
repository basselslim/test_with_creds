package controler;

import model.*;
import model.Map;
import view.Window;

import java.util.*;

/**
 * 
 */
public class RequestStateDeliveryPoint implements State {

    private Request request;
    private Intersection precedingPoint;

    /**
     * Default constructor
     */
    public RequestStateDeliveryPoint() {
    }

    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        if (precedingPoint == null) {
            precedingPoint = i;
            controller.TextMessage.setText("Select the delivery point");
        } else {
            DeliveryPoint delivery = new DeliveryPoint(i,0);
            request.setDeliveryPoint(delivery);
            controller.Gview.refreshMap();
            controller.TextMessage.setText(("Enter duration"));
        }
    }

    @Override
    public void addDuration(int duration, Controller controller){
        request.getDeliveryPoint().setDeliveryDuration(duration);
        controller.requestStateConfirmation.entryAction(request, controller);
        controller.setCurrentState(controller.requestStateConfirmation);

    }

    protected void entryAction(Controller controller, Request r) {
        request = r;
        controller.Gview.enableSelection();
        controller.TextMessage.setText(("Select the preceding point to the delivery point"));
    }

}