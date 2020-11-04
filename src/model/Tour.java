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
    protected List<int[]> listTimes;
    protected Map map;

    /**
     * Default constructor
     */
    public Tour(Map map) {
        this.listPaths = new LinkedList<Path>();
        this.listTimes = new LinkedList<int[]>();
        this.tourLength = 0;
        this.map = map;
    }

    public Tour(Map map, List<Path> listPaths) {
        this.listPaths = listPaths;
        this.listTimes = new LinkedList<int[]>();
        this.tourLength = 0;
        this.map = map;
        for(Path p: listPaths) {
            this.tourLength += p.getPathLength();
        }
        populateListTimes();
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

    public List<int[]> getListTimes() { return listTimes; }

    public void addPath(Path newPath) {
        listPaths.add(newPath);
        tourLength += newPath.pathLength;
        if (map.getDepot().getId() == listPaths.get(listPaths.size()-1).getIdArrival()) {
            populateListTimes();
        }
    }

    public int stringToTime(String s) {
        int indexEndHours = 0;
        while (s.charAt(indexEndHours) != ':') {
            indexEndHours++;
        }
        int hours = Integer.parseInt(s.substring(0, indexEndHours));
        int indexEndMinutes = indexEndHours + 2;
        while (indexEndMinutes < s.length() && s.charAt(indexEndMinutes) != ':') {
            indexEndMinutes++;
        }
        int minutes = Integer.parseInt(s.substring(indexEndHours+1, indexEndMinutes));
        int time = (hours * 3600) + (minutes * 60);
        return time;
    }

    public String timeToString(int t) {
        int hours = t / 3600;
        t -= hours * 3600;
        int minutes = t / 60;
        String minutesString;
        if (minutes == 0) {
            minutesString = "00";
        } else if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }
        String res = hours + ":" + minutesString;
        return res;
    }

    public void populateListTimes() {
        listTimes.clear();
        final double VELOCITY = 2.78;
        for (int i = 0; i < listPaths.size(); i++) {
            listTimes.add(new int[2]);
        }
        int time = stringToTime(map.getDepot().getDepartureTime());
        time = checkTimeUnderOneDay(time);
        listTimes.get(0)[0] = time;
        time += (int) listPaths.get(0).getPathLength() / VELOCITY;
        time = checkTimeUnderOneDay(time);
        listTimes.get(0)[1] = time;
        int indexPath = 1;
        for (int[] times : listTimes.subList(1, listTimes.size())) {
            long id = listPaths.get(indexPath).getIdDeparture();
            Intersection stop = map.getTourStopById(id);
            if (stop instanceof PickUpPoint)  {
                time += ((PickUpPoint) stop).getPickUpDuration();
            } else if (stop instanceof DeliveryPoint) {
                time += ((DeliveryPoint) stop).getDeliveryDuration();
            }
            time = checkTimeUnderOneDay(time);
            times[0] = time;
            time += (int) listPaths.get(indexPath).getPathLength() / VELOCITY;
            time = checkTimeUnderOneDay(time);
            times[1] = time;
            indexPath++;
        }
    }

    public int checkTimeUnderOneDay(int time) {
        while (time >= 86400) {
            time -= 86400;
        }
        return time;
    }
}