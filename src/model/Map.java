package model;

import java.util.*;

/**
 * A set of intersections connected by segments forming an oriented graph representing the different possible
 * delivery/pickup points, and the distance between each point.
 *
 * @author T-REXANOME
 */
public class Map extends observer.Observable {

    protected List<Request> listRequests;
    protected HashMap<Long, Intersection> listIntersections;
    protected Tour deliveryTour;
    protected Depot depot;

    /**
     * Default constructor.
     */
    public Map() {
        listIntersections = new HashMap<Long, Intersection>();
        listRequests = new ArrayList<>();
        depot = new Depot();
        deliveryTour = new Tour(this);
    }

    /**
     * Constructor.
     *
     * @param listIntersection map of all the intersections
     */
    public Map(HashMap<Long, Intersection> listIntersection) {
        this.listIntersections = listIntersection;
        listRequests = new ArrayList<>();
        depot = new Depot();
        deliveryTour = new Tour(this);
    }

    /**
     * Clear map.
     */
    public void clearMap() {
        listIntersections.clear();
        deliveryTour.getListPaths().clear();
    }

    /**
     * Clear requests list.
     */
    public void clearRequests() {
        listRequests.clear();
    }

    /**
     * Clear tour.
     */
    public void clearTour() {
        deliveryTour.listPaths.clear();
        deliveryTour.listRequestsIntersection.clear();
        deliveryTour.listTimes.clear();
    }

    /**
     * Get the minimum latitude.
     *
     * @return the minimum latitude
     */
    public double getMinLat() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() < min)
                min = intersection.getLatitude();
        }
        return min;
    }

    /**
     * Get the maximum latitude.
     *
     * @return the maximum latitude
     */
    public double getMaxLat() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() > max)
                max = intersection.getLatitude();
        }
        return max;
    }

    /**
     * Get the minimum longitude.
     *
     * @return the minimum longitude
     */
    public double getMinLong() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() < min)
                min = intersection.getLongitude();
        }
        return min;
    }

    /**
     * Get the maximum longitude.
     *
     * @return the maximum longitude
     */
    public double getMaxLong() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() > max)
                max = intersection.getLongitude();
        }
        return max;
    }

    /**
     * @param id id of a pick up or delivery point
     * @return
     */
    public Intersection getTourStopById(long id) {
        Intersection res = null;
        for (int i = 0; i < listRequests.size(); i++) {
            if (listRequests.get(i).getPickUpPoint().getId() == id) {
                res = listRequests.get(i).getPickUpPoint();
                break;
            }
            if (listRequests.get(i).getDeliveryPoint().getId() == id) {
                res = listRequests.get(i).getDeliveryPoint();
                break;
            }
        }
        return res;
    }

    /**
     * @param id id of an intersection
     * @return request
     */
    public Request getRequestByIntersectionId(long id) {
        Request res = null;
        for (int i = 0; i < listRequests.size(); i++) {
            if (listRequests.get(i).getPickUpPoint().getId() == id || listRequests.get(i).getDeliveryPoint().getId() == id) {
                res = listRequests.get(i);
                break;
            }
        }
        return res;
    }

    /*
     * Getters - Setters
     */

    //REQUESTS
    public List<Request> getListRequests() {
        return listRequests;
    }

    public void setListRequest(List<Request> l) {
        listRequests = l;
        notifyObservers();
    }

    /**
     * Find the departure intersection of a request, giving the arrival intersection.
     *
     * @param RequestPoint arrival intersection of a request
     * @return preceding intersection
     */
    public Step findPrecedingRequestPoint(Step RequestPoint){
        Step precedingPoint = null;

        for (Path path : deliveryTour.getListPaths()) {
            if (path.getArrival().getRequest() == RequestPoint.getRequest() && path.getIdArrival() == RequestPoint.getId())
                precedingPoint = path.getDeparture();
            }
        return precedingPoint;
    }

    //INTERSECTIONS

    /**
     * Add a request.
     *
     * @param newRequest          request to add
     * @param precedingPickUpId   id of the preceding pick up point
     * @param precedingDeliveryId id of the preceding delivery point
     * @return error code
     */
    public int addRequest(Request newRequest,Step precedingPickUpId,Step precedingDeliveryId) {
        int errorCode = this.deliveryTour.addRequestToTour(newRequest,precedingPickUpId,precedingDeliveryId);
        if (errorCode == 0) {
            this.listRequests.add(newRequest);
        }
        notifyObservers();
        return errorCode;
    }

    /**
     * Add a request.
     *
     * @param newRequest request to add
     * @return error code
     */
    public int addRequest(Request newRequest) {
        this.listRequests.add(newRequest);
        notifyObservers();
        return 0;
    }

    /**
     * Remove a request.
     *
     * @param request request to remove
     */
    public void removeRequest(Request request) {
        if (this.deliveryTour.getListPaths().size() == 0)
            this.listRequests.remove(request);
        else {
            this.deliveryTour.removeRequestFromTour(request);
            this.listRequests.remove(request);
        }

        this.notifyObservers();
    }

    //INTERSECTIONS
    public HashMap<Long, Intersection> getListIntersections() {
        return listIntersections;
    }

    public void setListIntersections(HashMap<Long, Intersection> l) {
        listIntersections = l;
        notifyObservers();
    }

    //DEPOT
    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    //TOUR
    public Tour getDeliveryTour() {
        return deliveryTour;
    }

    public void setDeliveryTour(Tour newTour) {
        this.deliveryTour = newTour;
        this.deliveryTour.populateListTimes();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Map{" +
                "listRequests=" + listRequests +
                ", listIntersections=" + listIntersections +
                '}';
    }

    public void resetMap() {
        listIntersections = new HashMap<Long, Intersection>();
        listRequests = new ArrayList<>();
        depot = new Depot();
        deliveryTour = new Tour(this);
    }

    public void resetRequests() {
        listRequests = new ArrayList<>();
        depot = new Depot();
        deliveryTour = new Tour(this);
    }
}