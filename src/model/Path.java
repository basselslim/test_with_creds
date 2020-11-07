package model;

import java.util.*;

/**
 * List of segments with a start and end intersection.
 *
 * @author T-REXANOME
 */
public class Path {

    protected int pathLength;
    protected List<Segment> listSegments;
    protected long idDeparture;
    protected long idArrival;

    /**
     * Default constructor
     */
    public Path() {
    }

    /**
     * Constructor
     *
     * @param listSegments list of segments of the path
     * @param departure    id of the departure intersection
     * @param arrival      id of the arrival intersection
     */
    public Path(List<Segment> listSegments, long departure, long arrival) {
        this.listSegments = listSegments;
        this.idDeparture = departure;
        this.idArrival = arrival;
        this.pathLength = 0;
        for (Segment s : listSegments) {
            this.pathLength += s.getLength();
        }
    }

    /*
     * Getters - Setters
     */

    public int getPathLength() {
        return pathLength;
    }

    public List<Segment> getListSegments() {
        return listSegments;
    }

    public int getDuration() {
        return (int) (((double) pathLength / 15.0) * 3.6);
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