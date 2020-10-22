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
    protected Long idDeparture;
    protected Long idArrival;
    /**
     * Default constructor
     */
    public Path() {
    }

    public Path(List<Segment> listSegments,Long departure, Long arrival) {
        this.listSegments = listSegments;
        idDeparture = departure;
        idArrival = arrival;
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

    public Long getIdDeparture() {
        return idDeparture;
    }

    public Long getIdArrival() {
        return idArrival;
    }

    public List<Segment> getListSegments() {
        return listSegments;
    }

    public int getDuration() {
        return (int) (((double) pathLength/15.0)*3.6);
    }
}