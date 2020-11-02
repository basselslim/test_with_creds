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

    public void leftClick(Controller controler, Map map, ListOfCommand listOfCommand, Intersection i);
}