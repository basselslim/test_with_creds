package model;

import java.util.*;

/**
 * 
 */
public class Intersection {
    /**
     *
     */

    protected long id;
    protected double latitude;
    protected double longitude;
    protected List<Segment> listSegments;

    /**
     * Default constructor
     */
    public Intersection() {
    }

    public Intersection(long id, double latitude, double longitude, List<Segment> listSegments) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.listSegments = listSegments;
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




    @Override
    public String toString() {
        return "Intersection{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", id=" + id +
                ", listSegments=" + listSegments +
                '}';
    }
}