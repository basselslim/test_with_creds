package controler;

import model.Map;
import model.Request;

/**
 * Create the command which adds the request r to the plan m.
 *
 * @author T-REXANOME
 */
public class AddCommand implements Command {

    private Map map;
    private Request request;
    private Long precedingPickUpId;
    private Long precedingDeliveryId;

    /**
     * Constructor.
     *
     * @param m the map to which f is added
     * @param r the request added to m
     */
    public AddCommand(Map m, Request r,Long pickUpId,Long deliveryId) {
        this.map = m;
        this.request = r;
        this.precedingPickUpId=pickUpId;
        this.precedingDeliveryId=deliveryId;
    }

    /**
     * Do.
     */
    @Override
    public void doCommand() {
        if(precedingDeliveryId !=0 && precedingPickUpId != 0)
            map.addRequest(request,precedingPickUpId,precedingDeliveryId);
        else
            map.addRequest(request);
    }

    /**
     * Undo.
     */
    @Override
    public void undoCommand() {
        map.removeRequest(request);
    }
}
