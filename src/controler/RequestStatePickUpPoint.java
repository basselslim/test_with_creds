package controler;

import model.*;
import model.Map;
import view.Window;

import java.util.*;

/**
 * @author T-REXANOME
 */
public class RequestStatePickUpPoint implements State {

    private Request request;
    private Intersection precedingPoint;

    /**
     * Default constructor
     */
    public RequestStatePickUpPoint() {
    }

    /**
     * Left click.
     *
     * @param controller
     * @param map
     * @param listOfCommand
     * @param i intersection
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {

        if (precedingPoint == null) {
            precedingPoint = new Intersection(i);
            controller.TextMessage.setText("Select the pickup point");
        } else {
            PickUpPoint pickup = new PickUpPoint(i, 0);
            request.setPickUpPoint(pickup);

            controller.TextMessage.setText(("Enter duration"));
            controller.addDuration(controller.Tview.durationPopup());

        }
    }

    /**
     *
     * @param duration   duration to add
     * @param controller
     */
    @Override
    public void addDuration(int duration, Controller controller) {
        request.getPickUpPoint().setPickUpDuration(duration);
        controller.Gview.disableSelection();

        controller.requestStateDeliveryPoint.entryAction(controller, request);
        controller.setCurrentState(controller.requestStateDeliveryPoint);
    }

    /**
     *
     * @param controller
     */
    protected void entryAction(Controller controller) {
        controller.Gview.enableSelection();
        request = new Request();
        precedingPoint = null;
        controller.TextMessage.setText("Select the preceding point to the pickup point");
    }
}