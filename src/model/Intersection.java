package model;

import java.util.*;

/**
 * 
 */
public class Intersection implements Comparable<Intersection>{
    /**
     *
     */

    protected long id;
    protected double latitude;
    protected double longitude;
    protected List<Segment> listSegments;

    // protected Intersection current;
    protected Intersection previous;
    protected double routeScore;
    protected double estimatedScore;

    /**
     * Default constructor
     */
    public Intersection() {
    }

    public Intersection(long id, double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.listSegments = new ArrayList<Segment>();
    }

    public Intersection(long id, double latitude, double longitude, List<Segment> listSegments) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.listSegments = listSegments;
    }

    public Intersection(Intersection current) {
        // this.current = current;
        this.previous = null;
        this.routeScore = Double.POSITIVE_INFINITY;
        this.estimatedScore = Double.POSITIVE_INFINITY;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    public Intersection(Intersection current, Intersection previous, double routeScore, double estimatedScore) {
        // this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    /**
     * Getters - Setters
     */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Segment> getListSegments() {
        return listSegments;
    }

    public void setListSegments(List<Segment> listSegments) {
        this.listSegments = listSegments;
    }
/*
    public Intersection getCurrent() {
        return current;
    }

    public void setCurrent(Intersection current) {
        this.current = current;
    } */

    public Intersection getPrevious() {
        return previous;
    }

    public void setPrevious(Intersection previous) {
        this.previous = previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(Intersection other) {
        if (this.estimatedScore > other.estimatedScore) {
            return 1;
        } else if (this.estimatedScore < other.estimatedScore) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", listSegments=" + listSegments +
                ", routeScore=" + routeScore +
                ", estimatedScore=" + estimatedScore +
                '}';
    }
}