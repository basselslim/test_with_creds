package model;

/**
 * Intersection for which a package must be delivered.
 *
 * @author T-REXANOME
 */
public class DeliveryPoint extends Intersection {

    public int deliveryDuration;

    /**
     * Default constructor
     */
    public DeliveryPoint() {
    }

    /**
     * Constructor
     *
     * @param intersection     intersection where the delivery point is
     * @param deliveryDuration estimated time for the deliverer to deliver the package
     */
    public DeliveryPoint(Intersection intersection, int deliveryDuration) {
        this.longitude = intersection.getLongitude();
        this.latitude = intersection.getLatitude();
        this.id = intersection.getId();
        this.listSegments = intersection.listSegments;
        this.deliveryDuration = deliveryDuration;
    }

    /**
     * Constructor
     *
     * @param id               id of the intersection
     * @param latitude         x coordinates
     * @param longitude        y coordinates
     * @param deliveryDuration estimated time for the deliverer to deliver the package
     */
    public DeliveryPoint(long id, double latitude, double longitude, int deliveryDuration) {
        super(id, latitude, longitude);
        this.deliveryDuration = deliveryDuration;
    }

    /*
     * Getters - Setters
     */

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(int deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }
}