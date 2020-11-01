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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", departureTime=" + departureTime +
                '}';
    }
}
