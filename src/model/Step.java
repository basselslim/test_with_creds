package model;

public class Step extends Intersection {

    protected Request request;

    public Step () {
    }

    public Step(long id, double latitude,double longitude) {
        super (id, latitude, longitude);
    }

    public void setRequest(Request request) { this.request = request; }

    public Request getRequest() { return request; }
}
