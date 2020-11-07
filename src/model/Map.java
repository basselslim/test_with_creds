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
     * Default constructor
     */
    public Map() {
        listIntersections = new HashMap<Long, Intersection>();
        listRequests = new ArrayList<>();
        depot = new Depot();
        deliveryTour = new Tour();
    }

    /**
     * Constructor
     *
     * @param listIntersection list of intersections of the map
     */
    public Map(HashMap<Long, Intersection> listIntersection) {
        this.listIntersections = listIntersection;
        listRequests = new ArrayList<>();
        depot = new Depot();
    }

    /**
     * Display.
     */
    public void display() {
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            System.out.println(intersection);

            for (Segment segment : intersection.getListSegments()) {
                System.out.println(segment);
            }
        }

        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            System.out.println(mapentry.getValue());
        }

        for (int i = 0; i < listRequests.size(); i++) {
            System.out.println(listRequests.get(i));
        }

        System.out.println(this.depot);
    }

    /**
     * Clear the map.
     */
    public void clearMap() {
        listIntersections.clear();
        deliveryTour.getListPaths().clear();
    }

    /**
     * Clear the request list.
     */
    public void clearRequests() {
        listRequests.clear();
    }

    /**
     * Find the minimum latitude.
     *
     * @return the minimum latitude
     */
    public double findMinLat() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() < min)
                min = intersection.getLatitude();
        }
        return min;
    }

    /**
     * Fine the maximum latitude.
     *
     * @return the maximum latitude
     */
    public double findMaxLat() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() > max)
                max = intersection.getLatitude();
        }
        return max;
    }

    /**
     * Find the minimum longitude.
     *
     * @return the minimum longitude
     */
    public double findMinLong() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() < min)
                min = intersection.getLongitude();
        }
        return min;
    }

    /**
     * Find the maximum longitude.
     *
     * @return the maximum longitude
     */
    public double findMaxLong() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() > max)
                max = intersection.getLongitude();
        }
        return max;
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

    //INTERSECTIONS

    /**
     * Add a request to the request list.
     *
     * @param r request to add
     */
    public void addRequest(Request r) {
        this.listRequests.add(r);
    }

    /**
     * Remove a request from the request list.
     *
     * @param r request to remove
     */
    public void removeRequest(Request r) {
        this.listRequests.remove(r);
    }

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
        notifyObservers();
    }

    //TOUR
    public Tour getTour() {
        return deliveryTour;
    }

    public void setDeliveryTour(Tour newTour) {
        this.deliveryTour = newTour;
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Map{" +
                "listRequests=" + listRequests +
                ", listIntersections=" + listIntersections +
                '}';
    }
}