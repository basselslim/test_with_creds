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
    protected int pickUpDuration;

    /**
     * Default constructor
     */
    public PickUpPoint() {
    }

    public PickUpPoint(Intersection intersection, int pickUpDuration) {
        this.longitude = intersection.getLongitude();
        this.latitude = intersection.getLatitude();
        this.id = intersection.getId();
        this.listSegments = intersection.listSegments;
        this.pickUpDuration = pickUpDuration;
    }


    public PickUpPoint( long id, double latitude,double longitude,  int pickUpDuration) {
        super(id, latitude, longitude);

        this.pickUpDuration = pickUpDuration;
    }

    /**
     * Getters - Setters
     */
    public int getPickUpDuration() {
        return pickUpDuration;
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }
}