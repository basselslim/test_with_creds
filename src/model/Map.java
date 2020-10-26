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

    protected HashMap<Long, List<Path>> mapSmallestPaths;
    protected Depot depot;


    /**
     * Default constructor
     */

    public Map(){
        listIntersections = new HashMap<Long,Intersection>();
        listRequests = new ArrayList<>();
        depot= new Depot();
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
            ;
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

    public HashMap<Long, List<Path>> getMapSmallestPaths() {
        return mapSmallestPaths;
    }

    public void setMapSmallestPaths(HashMap<Long, List<Path>> mapSmallestPaths) {
        this.mapSmallestPaths = mapSmallestPaths;
    }


    @Override
    public String toString() {
        return "Map{" +
                "listRequests=" + listRequests +
                ", listIntersections=" + listIntersections +
                '}';
    }
}