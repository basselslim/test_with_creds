package observer;

/**
 * Observer.
 *
 * @author T-REXANOME
 */
public interface Observer {

    /**
     * Update.
     *
     * @param observed
     * @param arg argument
     */
    public void update(Observable observed, Object arg);
}
