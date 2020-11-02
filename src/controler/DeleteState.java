package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.Window;

public class DeleteState implements State {
    // State of AGILEPLD when receiving the message delete() from InitialState
    // -> Wait for a leftClick
    // -> Return back to initialState when receiving the message rightClick()

    @Override
    public void leftClick(Controller controler, Map map, ListOfCommand listOfCommands, Intersection i) {
       /* Shape shape = plan.search(p);
        if (shape != null)
            listOfCommands.add(new ReverseCommand(new AddCommand(plan, shape))); */
    }

}