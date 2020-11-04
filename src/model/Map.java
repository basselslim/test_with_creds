package model;

import java.util.*;

/**
 * check for java beans for Observable
 */
public class Map extends observer.Observable {

    /**
     *
     */
    protected List<Request> listRequests;

    protected HashMap<Long,Intersection> listIntersections;

    protected Tour deliveryTour;
    protected Depot depot;


    /**
     * Default constructor
     */

    public Map() {
        listIntersections = new HashMap<Long, Intersection>();
        listRequests = new ArrayList<>();
        depot= new Depot();
        deliveryTour = new Tour(this);
    }


    public Map(HashMap<Long, Intersection> listIntersection) {
        this.listIntersections = listIntersection;
        listRequests = new ArrayList<>();
        depot = new Depot();
    }

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

    public void clearMap() {
        listIntersections.clear();
        deliveryTour.getListPaths().clear();
    }

    public void clearRequests() {
        listRequests.clear();
    }

    public double findMinLat() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() < min)
                min = intersection.getLatitude();
        }
        return min;
    }

    public double findMaxLat() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLatitude() > max)
                max = intersection.getLatitude();
        }
        return max;
    }

    public double findMinLong() {
        double min = 100;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() < min)
                min = intersection.getLongitude();
        }
        return min;
    }

    public double findMaxLong() {
        double max = 0;
        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getLongitude() > max)
                max = intersection.getLongitude();
        }
        return max;
    }

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
    public void addRequest(Request r) {
        this.listRequests.add(r);
    }

    public void removeRequest(Request r) {
        this.listRequests.remove(r);
    }

    public HashMap<Long,Intersection> getListIntersections() {
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