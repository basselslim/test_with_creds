package model;

import java.util.*;

/**
 * check for java beans for Observable
 */
public class Map extends Observable {

    /**
     * Default constructor
     */
    protected List<Intersection> listIntersection;

    public Map(){

    }

    public Map(List<Intersection> listIntersection) {
        this.listIntersection = listIntersection;
    }

    public List<Intersection> getListIntersection() {
        return listIntersection;
    }

    public void setListIntersection(List<Intersection> listIntersection) {
        this.listIntersection = listIntersection;
    }



    public void display(){
        int nbIntersection = listIntersection.size();
        for(int i = 0; i < nbIntersection; i++)
        {
            System.out.println(this.listIntersection.get(i));
        }
    }
}