package model;

import java.sql.Time;
import java.util.*;

/**
 * 
 */
public class PickUpPoint extends Intersection {
    /**
     *
     */
    protected Time pickUpDuration;

    /**
     * Default constructor
     */
    public PickUpPoint() {
    }

    public PickUpPoint(Intersection intersection, Time pickUpDuration) {
        this.longitude = intersection.getLongitude();
        this.latitude = intersection.getLatitude();
        this.id = intersection.getId();
        this.pickUpDuration = pickUpDuration;
    }

    public PickUpPoint( long id, double longitude, double latitude, Time pickUpDuration) {
        super(id, latitude, longitude);
        this.pickUpDuration = pickUpDuration;
    }

    /**
     * Getters - Setters
     */
    public Time getPickUpDuration() {
        return pickUpDuration;
    }

    public void setPickUpDuration(Time pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }
}