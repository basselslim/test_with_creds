package model;

import java.util.*;

/**
 * 
 */
public class Request extends Observable {
    /**
     *
     */
    protected int order;
    protected PickUpPoint pickUpPoint;
    protected DeliveryPoint deliveryPoint;

    /**
     * Default constructor
     */
    public Request() {
    }

    public Request(Request r) {
        this.order = r.order;
        this.pickUpPoint = r.getPickUpPoint();
        this.deliveryPoint = r.getDeliveryPoint();
    }

    public Request(PickUpPoint pickUpPoint, DeliveryPoint deliveryPoint) {
        this.pickUpPoint = pickUpPoint;
        this.deliveryPoint = deliveryPoint;
    }

    /**
     * Getters - Setters
     */
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public PickUpPoint getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(PickUpPoint pickUppoint) {
        this.pickUpPoint = pickUppoint;
    }

    public DeliveryPoint getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

}