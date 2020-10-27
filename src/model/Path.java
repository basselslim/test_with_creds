package model;

import java.util.*;

/**
 * 
 */
public class Path {
    /**
     *
     */
    protected int pathLength;
    protected List<Segment> listSegments;
    protected long idDeparture;
    protected long idArrival;

    /**
     * Default constructor
     */
    public Path() {
    }

    public Path(List<Segment> listSegments, long departure, long arrival) {
        this.listSegments = listSegments;
        this.idDeparture = departure;
        this.idArrival = arrival;
        this.pathLength = 0;
        for(Segment s: listSegments) {
            this.pathLength += s.getLength();
        }
    }

    /**
     * Getters - Setters
     */
    public int getPathLength() {
        return pathLength;
    }

    public List<Segment> getListSegments() {
        return listSegments;
    }

    public int getDuration() {
        return (int) (((double) pathLength/15.0)*3.6);
    }

    public long getIdDeparture() {
        return idDeparture;
    }

    public long getIdArrival() {
        return idArrival;
    }

    @Override
    public String toString() {
        return "Path{" +
                "pathLength=" + pathLength +
                ", idDeparture=" + idDeparture +
                ", idArrival=" + idArrival +
                '}';
    }
}