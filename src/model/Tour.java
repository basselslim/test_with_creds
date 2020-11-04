package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    protected String roadMapFilePath;
    protected HashMap<Long,ArrayList<Intersection>> listRequestsIntersection;
    /**
     * Default constructor
     *
     */
    public Tour(){
    }

    public Tour(Map map) {
        this.listPaths = new LinkedList<Path>();
        this.listTimes = new LinkedList<int[]>();
        this.tourLength = 0;
        this.map = map;
        this.listRequestsIntersection = new HashMap<Long,ArrayList<Intersection>>();
    }

    public Tour(Map map, List<Path> listPaths) {
        this.listPaths = listPaths;
        this.listTimes = new LinkedList<int[]>();
        this.tourLength = 0;
        this.map = map;
        for(Path p: listPaths) {
            this.tourLength += p.getPathLength();
        }
        this.listRequestsIntersection = new HashMap<Long,ArrayList<Intersection>>();
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

    public void groupRequestIntersections(){
        Intersection point;

        point = map.getListIntersections().get(map.getDepot().getId());
        if (point != null) {

            ArrayList intermediateList = this.listRequestsIntersection.get(map.getDepot().getId());
            if (intermediateList == null) {
                intermediateList= new ArrayList<Intersection>();
            }
            intermediateList.add(point);
            this.listRequestsIntersection.put(map.getDepot().getId(),intermediateList);
        }

        for (Request r: this.map.getListRequests()) {
            point = map.getListIntersections().get(r.getDeliveryPoint().getId());
            System.out.println("Delivery point trouvé"+point);
            if (point != null) {
                ArrayList intermediateList = this.listRequestsIntersection.get(r.getDeliveryPoint().getId());
                if (intermediateList == null) {
                    intermediateList= new ArrayList<Intersection>();
                }
                intermediateList.add(point);
                System.out.println("intermediate list added for delivery points"+intermediateList);
                this.listRequestsIntersection.put(r.getDeliveryPoint().getId(),intermediateList);
            }
            point = map.getListIntersections().get(r.getPickUpPoint().getId());
            System.out.println("Pick-up point trouvé"+point);
            if (point != null) {
                ArrayList intermediateList = this.listRequestsIntersection.get(r.getPickUpPoint().getId());
                if (intermediateList == null) {
                    intermediateList= new ArrayList<Intersection>();
                }
                intermediateList.add(point);
                this.listRequestsIntersection.put(r.getPickUpPoint().getId(),intermediateList);
            }
        }

        System.out.println("Etat de la liste");
        for (long i : this.listRequestsIntersection.keySet())
            System.out.println(this.listRequestsIntersection.get(i));
    }

    public String writeTextForInterestPoint(long id)
    {
        String text = "";
        System.out.println(" État  de la liste d'intersection " +id+ this.listRequestsIntersection.get(id));
        for (Intersection i : this.listRequestsIntersection.get(id)) {
            Intersection interestPoint = i;
            if (interestPoint instanceof PickUpPoint) {
                text+= "Pick up the associated package for this intersection\n";
            } else if (interestPoint instanceof DeliveryPoint) {
                text+="Deliver the associated package for this intersection\n";
            } else {
                text +="Départ du dépot";
            }
        }
        return text;
    }

    public String generateTextForRoadMap() {
        String totalText = "Roadmap \n\n\n";
        int i = 0;
        for(Path p: listPaths) {
            i++;
            String PathTitle = "Path n°" + i + ": from intersection " + p.idDeparture + " to intersection " + p.idArrival + ":\n\n";
            totalText+=PathTitle;
            int j = 0;
            totalText+=this.writeTextForInterestPoint(p.idDeparture);
            for(Segment s: p.getListSegments()) {
                j++;
                String SegmentDescription = "   "+ i +"."+j+": Take " + s.getStreetName() + " in direction of " + s.getDestination() + "\n\n";
                totalText+=SegmentDescription;
            }
            System.out.println("IdArrival is :" + p.idArrival);
            totalText+=this.writeTextForInterestPoint(p.idArrival);
            totalText+="\n";

        }
        return totalText;
    }

    public void generateRoadMap(String path)
    {
        try {
            File roadMap = new File(path);
            if (roadMap.createNewFile()) {
                System.out.println("File created: " + roadMap.getName());
                System.out.println("Absolute path: " + roadMap.getAbsolutePath());
                this.roadMapFilePath = roadMap.getAbsolutePath();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(this.roadMapFilePath);
            myWriter.write(this.generateTextForRoadMap());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

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