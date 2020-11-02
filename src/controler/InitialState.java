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

    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommand, Intersection i) {


        controller.setCurrentState(controller.requestStateDeliveryPoint);
    }


}