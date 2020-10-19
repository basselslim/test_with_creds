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
    protected List<Intersection> listIntersections;

    /**
     * Default constructor
     */
    protected List<Intersection> listIntersection;

    public Map(){

    }

    public Map(List<Intersection> listIntersection) {
        this.listIntersection = listIntersection;
    }

    public List<Intersection> getListIntersection() {
        return listIntersection;
    }

    public void setListIntersection(List<Intersection> listIntersection) {
        this.listIntersection = listIntersection;
    }



    public void display(){
        int nbIntersection = listIntersection.size();
        for(int i = 0; i < nbIntersection; i++)
        {
            System.out.println(this.listIntersection.get(i));
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

    public List<Intersection> getListIntersections() {
        return listIntersections;
    }

    public void setListIntersections(List<Intersection> listIntersections) {
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