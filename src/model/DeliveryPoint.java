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
    public int deliveryDuration;

    /**
     * Default constructor
     */
    public DeliveryPoint() {
    }

    public DeliveryPoint(long id, double latitude,double longitude,  int deliveryDuration) {
        super(id, latitude, longitude);
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Getters - Setters
     */
    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}