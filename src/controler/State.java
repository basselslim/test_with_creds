package controler;

import model.Intersection;
import model.Request;
import model.Map;
import view.Window;

import java.util.*;

/**
 * 
 */
public interface State {

    public default void leftClick(Controller controler, Map map, ListOfCommand listOfCommand, Intersection i){};

    public default void addDuration(int duration, Controller controller){};

    public default void addRequest(Controller controller){};

    public default void confirmRequest(Controller controller,Map map){};
}