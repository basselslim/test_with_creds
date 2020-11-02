package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.Window;

import java.util.*;

/**
 * 
 */
public class InitialState implements State {

    /**
     * Default constructor
     */
    public InitialState() {
    }

    @Override
    public void addRequest(Controller controller){

        controller.setCurrentState(controller.requestStateDeliveryPoint);
    }


}