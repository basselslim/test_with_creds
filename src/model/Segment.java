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
     * @param length      length in meters of the street
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

    @Override
    public String toString() {
        return "Segment{" +
                "length=" + length +
                ", street name=" + streetName +
                ", destination=" + destination +
                '}';
    }
}