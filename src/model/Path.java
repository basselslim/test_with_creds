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
    protected Intersection departure;
    protected Intersection arrival;

    /**
     * Default constructor
     */
    public Path() {
    }

    public Path(List<Segment> listSegments, Intersection a, Intersection b) {
        departure = a;
        arrival = b;
        this.idDeparture = a.getId();
        this.idArrival = b.getId();
        this.pathLength = 0;
        if (listSegments != null) {
            this.listSegments = listSegments;
            for (Segment s : listSegments) {
                this.pathLength += s.getLength();
            }
        } else {
            this.listSegments = new LinkedList<Segment>();
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
        return (int) (((double) pathLength / 15.0) * 3.6);
    }

    public long getIdDeparture() {
        return idDeparture;
    }

    public long getIdArrival() {
        return idArrival;
    }

    public Intersection getDeparture() { return departure; }

    public Intersection getArrival() { return arrival; }


    @Override
    public String toString() {
        return "Path{" +
                "pathLength=" + pathLength +
                ", idDeparture=" + idDeparture +
                ", idArrival=" + idArrival +
                '}';
    }
}