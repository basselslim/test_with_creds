package controler;

import model.Map;
import model.Request;
import model.Step;

/**
 * Create the command which adds the request r to the plan m.
 *
 * @author T-REXANOME
 */
public class AddCommand implements Command {

    private Map map;
    private Request request;
    private Step precedingPickUp;
    private Step precedingDelivery;

    /**
     * Constructor.
     *
     * @param m        the map to which f is added
     * @param r        the request added to m
     * @param pickUp   pick up point
     * @param delivery delivery point
     */
    public AddCommand(Map m, Request r, Step pickUp, Step delivery) {
        this.map = m;
        this.request = r;
        this.precedingPickUp = pickUp;
        this.precedingDelivery = delivery;
    }

    /**
     * Do action.
     */
    @Override
    public int doCommand() {
        int error = 0;
        if (precedingDelivery != null && precedingPickUp != null) {
            error = map.addRequest(request, precedingPickUp, precedingDelivery);
        } else {
            map.addRequest(request);
        }
        return error;
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     */
    @Override
    public int undoCommand() {
        map.removeRequest(request);
        return 0;
    }
}
