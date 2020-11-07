package model;

import java.util.*;

/**
 * Point characterized by a longitude and a latitude, which can be the origin or destination point of one or more
 * segments.
 *
 * @author T-REXANOME
 */
public class Intersection extends observer.Observable implements Comparable<Intersection> {

    protected long id;
    protected double latitude;
    protected double longitude;
    protected List<Segment> listSegments;

    protected Intersection previous;
    protected double routeScore;
    protected double estimatedScore;
    protected String type;

    /**
     * Default constructor
     */
    public Intersection() {
    }

    /**
     * Constructor
     *
     * @param id        id of the intersection
     * @param latitude  x coordinates
     * @param longitude y coordinates
     */
    public Intersection(long id, double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.listSegments = new ArrayList<Segment>();
    }

    /**
     * Constructor
     *
     * @param id           id of the intersection
     * @param latitude     x coordinates
     * @param longitude    y coordinates
     * @param listSegments list of segments which start from the intersection
     */
    public Intersection(long id, double latitude, double longitude, List<Segment> listSegments) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.listSegments = listSegments;
    }

    /**
     * Constructor used in the algorithm of smallest paths
     *
     * @param current intersection
     */
    public Intersection(Intersection current) {
        this.previous = null;
        this.routeScore = Double.POSITIVE_INFINITY;
        this.estimatedScore = Double.POSITIVE_INFINITY;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    /**
     * Constructor used in the algorithm of smallest paths
     *
     * @param current        intersection
     * @param previous       intersection
     * @param routeScore     score that represents the cheapest path from start
     * @param estimatedScore score that represents the estimated distance from start to the end
     */
    public Intersection(Intersection current, Intersection previous, double routeScore, double estimatedScore) {
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;

        this.longitude = current.longitude;
        this.latitude = current.latitude;
        this.id = current.id;
        this.listSegments = current.listSegments;
    }

    /*
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

    /**
     * Add a segment to the list of segments of the intersection.
     *
     * @param s segment to add
     */
    public void addSegment(Segment s) {
        listSegments.add(s);
        notifyObservers();
    }

    /**
     * Compare the estimated score of two intersections.
     *
     * @param other the intersection to compare to
     * @return 1 if the other intersection has a lower estimated score, -1 if it has a higher estimated score, or 0 if
     * they are equal.
     */
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