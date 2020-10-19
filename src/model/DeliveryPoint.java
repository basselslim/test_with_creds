package model;

import java.sql.Time;
import java.util.*;

/**
 * 
 */
public class DeliveryPoint extends Intersection {
    /**
     *
     */
    public Time deliveryDuration;

    /**
     * Default constructor
     */
    public DeliveryPoint() {
    }

    public DeliveryPoint(Intersection intersection, Time deliveryDuration) {
        this.longitude = intersection.getLongitude();
        this.latitude = intersection.getLatitude();
        this.id = intersection.getId();
        this.deliveryDuration = deliveryDuration;
    }

    public DeliveryPoint(long longitude, long latitude, long id, Time deliveryDuration) {
        super(longitude, latitude, id);
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Getters - Setters
     */
    public Time getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(Time deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}