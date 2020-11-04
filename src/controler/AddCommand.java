package controler;

import model.Map;
import model.Request;

public class AddCommand implements Command {

    private Map map;
    private Request request;
    private Long precedingPickUpId;
    private Long precedingDeliveryId;
    /**
     * Create the command which adds the request r to the plan m
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

    @Override
    public void doCommand() {
        map.addRequest(request,precedingPickUpId,precedingDeliveryId);
    }

    @Override
    public void undoCommand() {
        map.removeRequest(request);
    }
}
