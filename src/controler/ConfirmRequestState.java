package controler;

import model.Map;
import model.Request;


public class ConfirmRequestState implements State {

    Request request;

    public ConfirmRequestState() {
    }

    @Override
    public void confirmRequest(Controller controller, Map map) {
/**
 *
 */
        if(request != null) {
            AddCommand addRequestCommand = new AddCommand(controller.map, request);
            controller.getListOfCommand().add(addRequestCommand);
            long precedingDeliveryId = controller.addDeliveryState.getpreceding().getId();
            long precedingPickupId = controller.addPickupState.getpreceding().getId();
            //map.addtoTour(request,precedingPickupId,precedingDeliveryId);
            controller.setCurrentState(controller.initialState);
            controller.TextMessage.setText("Request added");
            controller.Gview.disableSelection();
        }
    }

    @Override
    public void undo(ListOfCommand listOfCommand, Controller controller) {
        controller.setCurrentState(controller.addDeliveryState);
    }

    @Override
    public void redo(ListOfCommand listOfCommand, Controller controller) {

    }


    protected void entryAction(Request r, Controller controller) {
        controller.Gview.disableSelection();
        request = r;
        controller.TextMessage.setText("Confirm adding the request ?");

    }
}