package model;

/**
 * Oriented line characterised by its origin and destination intersection, as well as its length and address.
 *
 * @author T-REXANOME
 */
public class Segment {

    protected double length;
    protected String streetName;
    protected long destination;

    /**
     * Default constructor
     */
    public Segment() {
    }

    /**
     * Constructor
     *
     * @param length      length of the street in meters
     * @param streetName  street name
     * @param destination id of the destination intersection
     */
    public Segment(double length, String streetName, long destination) {
        this.length = length;
        this.streetName = streetName;
        this.destination = destination;
    }

    /*
     * Getters - Setters
     */

    public double getLength() {
        return length;
    }

    public String getStreetName() {
        return streetName;
    }

    public long getDestination() {
        return destination;
    }

}