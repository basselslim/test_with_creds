package model;

import java.util.*;

/**
 * check for java beans for Observable
 */
public class Map extends Observable {

    /**
     *
     */
    protected List<Request> listRequests;

    protected HashMap<Long,Intersection> listIntersections;

    protected Tour deliveryTour;

    protected HashMap<Long,Intersection> listRequestsIntersection;

    protected Depot depot;


    /**
     * Default constructor
     */

    public Map(){
        listIntersections = new HashMap<Long,Intersection>();
        listRequests = new ArrayList<>();
        depot= new Depot();
        deliveryTour = new Tour();
    }


    public Map(HashMap<Long,Intersection> listIntersection) {
        this.listIntersections = listIntersection;
        listRequests = new ArrayList<>();
        depot= new Depot();
    }


    public void setListIntersection(HashMap<Long,Intersection> listIntersection) {
        this.listIntersections = listIntersection;
        listRequests = new ArrayList<>();
        depot= new Depot();

    }

    public void display(){

        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            System.out.println(intersection);

            for ( Segment segment : intersection.getListSegments()) {
                System.out.println(segment);
            }

        }

        for (HashMap.Entry mapentry : listIntersections.entrySet()) {
            System.out.println(mapentry.getValue());
        }

        for ( int i=0; i<listRequests.size(); i++ ) {
            System.out.println(listRequests.get(i));
        }

        System.out.println(this.depot);
    }

    public void clearMap(){
        listIntersections.clear();
    }

    public void clearRequests(){
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

    public void generateHashMapOfRequestsIntersection(){
        List<Intersection> listPoints = new ArrayList<>();

        Intersection point = this.getListIntersections().get(this.getDepot().getId());
        if (point != null) {
            listPoints.add(point);
        }

        for (Request r: this.listRequests) {
            point = this.getListIntersections().get(r.getDeliveryPoint().getId());
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
            point = this.getListIntersections().get(r.getPickUpPoint().getId());
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
        }
    }

    /**
     * Getters - Setters
     */

    public List<Request> getListRequests() {
        return listRequests;
    }

    public void setListRequests(List<Request> listRequests) {
        this.listRequests = listRequests;
    }


    public HashMap<Long,Intersection> getListIntersections() {
        return listIntersections;
    }

    public void setListIntersections(HashMap<Long,Intersection>listIntersections) {

        this.listIntersections = listIntersections;
    }

    public Depot getDepot() {
        return depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Tour getTour() {
        return deliveryTour;
    }

    public void setDeliveryTour(Tour newTour) {
        this.deliveryTour = newTour;
    }


    @Override
    public String toString() {
        return "Map{" +
                "listRequests=" + listRequests +
                ", listIntersections=" + listIntersections +
                '}';
    }
}