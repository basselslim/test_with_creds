package model;

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
        this.pickUpDuration = pickUpDuration;
    }

    public PickUpPoint(double longitude, double latitude, long id, int pickUpDuration) {
        super(id, longitude, latitude);
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