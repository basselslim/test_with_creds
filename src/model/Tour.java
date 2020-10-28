package model;

import java.util.*;

/**
 * 
 */
public class Tour extends Observable {
    /**
     *
     */
    protected int tourLength;
    protected List<Path> listPaths;

    /**
     * Default constructor
     */
    public Tour() {
        this.listPaths = new LinkedList<Path>();
        this.tourLength = 0;
    }

    public Tour(List<Path> listPaths) {
        this.listPaths = listPaths;
        this.tourLength = 0;
        for(Path p: listPaths) {
            this.tourLength += p.getPathLength();
        }
    }

    /**
     * Getters - Setters
     */
    public int getTourLength() {
        return tourLength;
    }

    public List<Path> getListPaths() {
        return listPaths;
    }

    public void addPath(Path newPath) {
        listPaths.add(newPath);
        tourLength += newPath.pathLength;
    }
}