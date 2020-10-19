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
    protected HashMap<Long, Intersection> listIntersections;

    /**
     * Default constructor
     */

    public Map(){

    }

    public Map(List<Intersection> listIntersection) {
        this.listIntersections = listIntersections;
    }

    public void display(){
        int nbIntersection = listIntersections.size();
        for(int i = 0; i < nbIntersection; i++)
        {
            System.out.println(this.listIntersections.get(i));
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

    public HashMap<Long, Intersection> getListIntersections() {
        return listIntersections;
    }

    public void setListIntersections(HashMap<Long, Intersection> listIntersections) {
        this.listIntersections = listIntersections;
    }



    @Override
    public String toString() {
        return "Map{" +
                "listRequests=" + listRequests +
                ", listIntersections=" + listIntersections +
                '}';
    }
}