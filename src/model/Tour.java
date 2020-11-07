package model;

import java.util.*;

/**
 * List of the steps to be carried out by the deliverer in an outing, and the path to be covered between each of these
 * steps.
 *
 * @author T-REXANOME
 */
public class Tour extends Observable {

    protected int tourLength;
    protected List<Path> listPaths;

    /**
     * Default constructor
     */
    public Tour() {
        this.listPaths = new LinkedList<Path>();
        this.tourLength = 0;
    }

    /**
     * Constructor
     *
     * @param listPaths
     */
    public Tour(List<Path> listPaths) {
        this.listPaths = listPaths;
        this.tourLength = 0;
        for (Path p : listPaths) {
            this.tourLength += p.getPathLength();
        }
    }

    /*
     * Getters - Setters
     */

    public int getTourLength() {
        return tourLength;
    }

    public List<Path> getListPaths() {
        return listPaths;
    }

    /**
     * Add a path to the path list.
     *
     * @param newPath path to add
     */
    public void addPath(Path newPath) {
        listPaths.add(newPath);
        tourLength += newPath.pathLength;
    }
}