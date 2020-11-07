package model;

/**
 * Intersection for which a package must be picked up.
 *
 * @author T-REXANOME
 */
public class PickUpPoint extends Intersection {

    protected int pickUpDuration;

    /**
     * Default constructor
     */
    public PickUpPoint() {
    }

    /**
     * Constructor
     *
     * @param intersection   intersection where the delivery point is
     * @param pickUpDuration estimated time for the deliverer to pick up the package
     */
    public PickUpPoint(Intersection intersection, int pickUpDuration) {
        this.longitude = intersection.getLongitude();
        this.latitude = intersection.getLatitude();
        this.id = intersection.getId();
        this.pickUpDuration = pickUpDuration;
    }

    /**
     * Constructor
     *
     * @param id             id of the intersection
     * @param latitude       x coordinates
     * @param longitude      y coordinates
     * @param pickUpDuration estimated time for the deliverer to pick up the package
     */
    public PickUpPoint(long id, double latitude, double longitude, int pickUpDuration) {
        super(id, latitude, longitude);

        this.pickUpDuration = pickUpDuration;
    }

    /*
     * Getters - Setters
     */

    public int getPickUpDuration() {
        return pickUpDuration;
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }
}