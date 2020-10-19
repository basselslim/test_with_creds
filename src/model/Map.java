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
    protected List<Segment> listSegment;

    public Map(){

    }

    public Map(List<Intersection> listIntersection, List<Segment> listSegment) {
        this.listIntersection = listIntersection;
        this.listSegment = listSegment;
    }

    public List<Intersection> getListIntersection() {
        return listIntersection;
    }

    public void setListIntersection(List<Intersection> listIntersection) {
        this.listIntersection = listIntersection;
    }

    public List<Segment> getListSegment() {
        return listSegment;
    }

    public void setListSegment(List<Segment> listSegment) {
        this.listSegment = listSegment;
    }

    public void display(){
        int nbIntersection = listIntersection.size();
        int nbSegment = listSegment.size();
        for(int i = 0; i < nbIntersection; i++)
        {
            System.out.println(this.listIntersection.get(i));
        }
        for(int i = 0; i < nbSegment; i++)
        {
            System.out.println(this.listSegment.get(i));
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