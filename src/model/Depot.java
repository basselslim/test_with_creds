package model;

import java.sql.Time;

public class Depot {

    protected long id;
    protected String departureTime;

    public Depot() {
    }

    public Depot(long id,String departureTime) {
        this.id = id;
        this.departureTime=departureTime;
    }

    /**
     * Getters - Setters
     */
    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                '}';
    }

    public long getId() {
        return id;
    }
}
