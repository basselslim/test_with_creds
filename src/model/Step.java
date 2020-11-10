package model;

public class Step extends Intersection {

    protected Request request;

    public Step () {
    }

    public Step(Step current) {
        this.request = new Request(current.getRequest());
        this.previous = null;
        this.routeScore = Double.POSITIVE_INFINITY;
        this.estimatedScore = Double.POSITIVE_INFINITY;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    public Step(long id, double latitude,double longitude) {
        super (id, latitude, longitude);
    }

    public void setRequest(Request request) { this.request = request; }

    public Request getRequest() { return request; }
}
