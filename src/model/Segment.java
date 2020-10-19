package model;

import java.util.*;

/**
 * 
 */
public class Segment {
    /**
     *
     */
    protected double length;
    protected String streetName;
    protected long destination;

    /**
     * Default constructor
     */
    public Segment() {
    }

    public Segment(double length, String streetName, long destination) {
        this.length = length;
        this.streetName = streetName;
        this.destination = destination;
    }

    /**
     * Getters - Setters
     */
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public long getDestination() {
        return destination;
    }

    public void setDestination(long destination) {
        this.destination = destination;
    }
}