package controler;

import model.Intersection;
import model.Map;
import model.Request;
import view.Window;

/**
 * State of AGILEPLD when receiving the message delete() from InitialState
 * -> Wait for a leftClick
 * -> Return back to initialState when receiving the message rightClick()
 *
 * @author T-REXANOME
 */
public class DeleteState implements State {

    /**
     * Left click.
     
     * @param controller
     * @param map
     * @param listOfCommands
     * @param i
     */
    @Override
    public void leftClick(Controller controller, Map map, ListOfCommand listOfCommands, Intersection i) {
       /* Shape shape = plan.search(p);
        if (shape != null)
            listOfCommands.add(new ReverseCommand(new AddCommand(plan, shape))); */
    }

    public void entryAction(Controller controller, Request request) {
        this.request = request;
        controller.Tview.setMessage("Confirm deleting the selected Request ?");
    }

}