package model;

/**
 * @author T-REXANOME
 */
public class Step extends Intersection {

    protected Request request;

    /**
     * Default constructor.
     */
    public Step() {
    }

    /**
     * Constructor.
     *
     * @param current step
     */
    public Step(Step current) {
        this.request = current.request;
        this.previous = null;
        this.routeScore = Double.POSITIVE_INFINITY;
        this.estimatedScore = Double.POSITIVE_INFINITY;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    /**
     * Constructor.
     *
     * @param id        intersection id
     * @param latitude  latitude of the point
     * @param longitude longitude of the point
     */
    public Step(long id, double latitude, double longitude) {
        super(id, latitude, longitude);
    }

    /*
     * Getters - Setters
     */

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
