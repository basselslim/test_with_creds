package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.Window;

import java.util.*;

/**
 * @author T-REXANOME
 */
public class InitialState implements State {

    /**
     * Default constructor
     */
    public InitialState() {
    }

    /**
     *
     * @param controller
     */
    @Override
    public void addRequest(Controller controller){
        controller.requestStatePickUpPoint.entryAction(controller);
        controller.setCurrentState(controller.requestStatePickUpPoint);

    }


}