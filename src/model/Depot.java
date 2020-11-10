package model;

import java.sql.Time;

/**
 * Departure and arrival point of the deliveryman for an outing.
 *
 * @author T-REXANOME
 */
public class Depot extends Step {

    protected long id;
    protected String departureTime;

    /**
     * Default constructor.
     */
    public Depot() {
    }

    /**
     * Constructor.
     *
     * @param id            id of the intersection
     * @param departureTime departure time (string)
     */
    public Depot(long id, String departureTime) {
        this.id = id;
        this.departureTime = departureTime;
    }

    /*
     * Getters - Setters
     */

    public long getId() {
        return id;
    }

    public String getDepartureTime() {
        return departureTime;
    }
}
