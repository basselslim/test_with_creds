package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.GraphicalView;
import view.Window;

import java.util.*;


public class RequestStateConfirmation implements State {

    Request request;
    public RequestStateConfirmation() {
    }

    @Override
    public void confirmRequest(Controller controller, Map map) {

        if(request != null) {
            map.getListRequests().add(request);
            controller.setCurrentState(controller.initialState);
            controller.TextMessage.setText("Request added");
            controller.Gview.disableSelection();
        }
    }


    protected void entryAction(Request r, Controller controller) {
        request = r;
        controller.TextMessage.setText("Confirm adding the request ?");

    }
}