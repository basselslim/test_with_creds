package observer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Observable
 *
 * @author T-REXANOME
 */
public class Observable {

    private Collection<Observer> obs;

    /**
     * Default constructor
     */
    public Observable() {
        obs = new ArrayList<Observer>();
    }

    /**
     * Add an observer to the observer collection
     *
     * @param o observer to add
     */
    public void addObserver(Observer o) {
        if (!obs.contains(o)) obs.add(o);
    }

    /**
     * Notify observers with an argument
     *
     * @param arg argument
     */
    public void notifyObservers(Object arg) {
        for (Observer o : obs)
            o.update(this, arg);
    }

    /**
     * Notify observers without arguments
     */
    public void notifyObservers() {
        notifyObservers(null);
    }
}