package model;

import controler.ComputeSmallestPath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * List of the steps to be carried out by the deliverer in an outing, and the path to be covered between each of these
 * steps.
 *
 * @author T-REXANOME
 */
public class Tour extends Observable {
    protected List<Path> listPaths;
    protected List<int[]> listTimes;
    protected Map map;
    protected String roadMapFilePath;
    protected HashMap<Long, ArrayList<Intersection>> listRequestsIntersection;

    /**
     * Default constructor.
     */
    public Tour() {
    }

    /**
     * Constructor.
     *
     * @param map
     */
    public Tour(Map map) {
        this.listPaths = new LinkedList<Path>();
        this.listTimes = new LinkedList<int[]>();
        this.map = map;
        this.listRequestsIntersection = new HashMap<Long, ArrayList<Intersection>>();
    }

    /**
     * Constructor.
     *
     * @param map
     * @param listPaths list of paths
     */
    public Tour(Map map, List<Path> listPaths) {
        this.listPaths = listPaths;
        this.listTimes = new LinkedList<int[]>();
        this.map = map;
        this.listRequestsIntersection = new HashMap<Long,ArrayList<Intersection>>();
        populateListTimes();
    }

    /**
     * Add a request to the tour.
     *
     * @param newRequest request to add
     * @param precedingPickUpId   id of the preceding pick up point
     * @param precedingDeliveryId id of the preceding delivery point
     * @return error code
     */
    public int addRequestToTour(Request newRequest, Long precedingPickUpId, Long precedingDeliveryId) {
        ComputeSmallestPath calculator = new ComputeSmallestPath(map);
        int pathIndexToInsertPickUp = 0;
        int pathIndexToInsertDelivery = 0;

        //Find the place to insert the pickUp
        for (Path path : listPaths) {
            if (path.getIdDeparture() == precedingPickUpId) {
                //Compute the shortest path between the Step preceding the pickup and the Request pickupPoint
                List<Segment> roadDeparturetoNewPickUp = calculator.computeSmallestPath(map.getListIntersections().get(path.getIdDeparture()), map.getListIntersections().get(newRequest.getPickUpPoint().getId()));
                if (roadDeparturetoNewPickUp == null) {
                    return 1; /* can't find a path to the new pick up point*/
                }
                List<Segment> roadNewPickUptoArrival = calculator.computeSmallestPath(map.getListIntersections().get(newRequest.getPickUpPoint().getId()), map.getListIntersections().get(path.getIdArrival()));
                if (roadNewPickUptoArrival == null) {
                    return 1; /* can't find a path to from new pick up point*/
                }

                // Create the path and insert them into the tour List
                Path pathDeparturetoNewPickUp = new Path(roadDeparturetoNewPickUp, path.getIdDeparture(), newRequest.getPickUpPoint().getId());
                Path pathNewPickUptoArrival = new Path(roadNewPickUptoArrival, newRequest.getPickUpPoint().getId(), path.getIdArrival());
                listPaths.remove(pathIndexToInsertPickUp);
                listPaths.add(pathIndexToInsertPickUp, pathDeparturetoNewPickUp);
                listPaths.add(pathIndexToInsertPickUp + 1, pathNewPickUptoArrival);
                break;
            }
            pathIndexToInsertPickUp++;
        }

        //Find the place to insert the Delivery
        for (Path path : listPaths) {
            if (path.getIdDeparture() == precedingDeliveryId) {
                //Compute the shortest path between the Step preceding the Delivery and the Request Delivery
                List<Segment> roadDeparturetoNewDelivery = calculator.computeSmallestPath(map.getListIntersections().get(path.getIdDeparture()), map.getListIntersections().get(newRequest.getDeliveryPoint().getId()));
                if (roadDeparturetoNewDelivery == null) {
                    return 2; /* can't find a path to the new delivery point*/
                }
                List<Segment> roadNewDeliverytoArrival = calculator.computeSmallestPath(map.getListIntersections().get(newRequest.getDeliveryPoint().getId()), map.getListIntersections().get(path.getIdArrival()));
                if (roadNewDeliverytoArrival == null) {
                    return 2; /* can't find a path from the new delivery point*/
                }

                // Create the paths and insert them into the tour List
                Path pathDeparturetoNewDelivery = new Path(roadDeparturetoNewDelivery, path.getIdDeparture(), newRequest.getDeliveryPoint().getId());
                Path pathNewDeliverytoArrival = new Path(roadNewDeliverytoArrival, newRequest.getDeliveryPoint().getId(), path.getIdArrival());
                listPaths.remove(pathIndexToInsertDelivery);
                listPaths.add(pathIndexToInsertDelivery, pathDeparturetoNewDelivery);
                listPaths.add(pathIndexToInsertDelivery + 1, pathNewDeliverytoArrival);
                break;
            }
            pathIndexToInsertDelivery++;
        }

        populateListTimes();
        return 0;
    }

    /**
     * Remove a request from the tour.
     *
     * @param request request to remove
     */
    public void removeRequestFromTour(Request request) {
        ComputeSmallestPath calculator = new ComputeSmallestPath(map);
        int pathIndexToDeletePickUp = 0;
        int pathIndexToDeleteDelivery = 0;
        Intersection departure = null;

        for (Path path : listPaths) {
            if (path.getIdArrival() == request.getPickUpPoint().getId()) {
                departure = map.getListIntersections().get(path.getIdDeparture());
            }
            if (path.getIdDeparture() == request.getPickUpPoint().getId()) {
                List<Segment> roadWithoutPickUpPoint = calculator.computeSmallestPath(departure, map.getListIntersections().get(path.getIdArrival()));
                Path pathWithoutPickUpPoint = new Path(roadWithoutPickUpPoint, departure.getId(), path.getIdArrival());
                listPaths.remove(pathIndexToDeletePickUp);
                listPaths.remove(pathIndexToDeletePickUp - 1);
                listPaths.add(pathIndexToDeletePickUp - 1, pathWithoutPickUpPoint);
                break;
            }
            pathIndexToDeletePickUp++;
        }

        for (Path path : listPaths) {
            if (path.getIdArrival() == request.getDeliveryPoint().getId()) {
                departure = map.getListIntersections().get(path.getIdDeparture());
            }
            if (path.getIdDeparture() == request.getDeliveryPoint().getId()) {
                List<Segment> roadWithoutDeliveryPoint = calculator.computeSmallestPath(departure, map.getListIntersections().get(path.getIdArrival()));
                Path pathWithoutDeliveryPoint = new Path(roadWithoutDeliveryPoint, departure.getId(), path.getIdArrival());
                listPaths.remove(pathIndexToDeleteDelivery);
                listPaths.remove(pathIndexToDeleteDelivery - 1);
                listPaths.add(pathIndexToDeleteDelivery - 1, pathWithoutDeliveryPoint);
                break;
            }
            pathIndexToDeleteDelivery++;
        }
        populateListTimes();
    }

    /*
     * Getters - Setters
     */

    /**
     * Compute tour length.
     *
     * @return tour length
     */
    public int getTourLength() {
        int tourLength =0;
        for (Path p : listPaths) {
            tourLength += p.getPathLength();
        }
        return tourLength;
    }

    public List<Path> getListPaths() {
        return listPaths;
    }

    public List<int[]> getListTimes() {
        return listTimes;
    }

    /**
     *
     */
    public void groupRequestIntersections() {
        Intersection point;

        point = map.getListIntersections().get(map.getDepot().getId());
        if (point != null) {

            ArrayList intermediateList = this.listRequestsIntersection.get(map.getDepot().getId());
            if (intermediateList == null) {
                intermediateList = new ArrayList<Intersection>();
            }
            intermediateList.add(point);
            this.listRequestsIntersection.put(map.getDepot().getId(), intermediateList);
        }

        for (Request r : this.map.getListRequests()) {
            point = r.getDeliveryPoint();
            if (point != null) {
                ArrayList intermediateList = this.listRequestsIntersection.get(r.getDeliveryPoint().getId());
                if (intermediateList == null) {
                    intermediateList = new ArrayList<Intersection>();
                }
                intermediateList.add(point);
                this.listRequestsIntersection.put(r.getDeliveryPoint().getId(), intermediateList);
            }
            point = r.getPickUpPoint();
            if (point != null) {
                ArrayList intermediateList = this.listRequestsIntersection.get(r.getPickUpPoint().getId());
                if (intermediateList == null) {
                    intermediateList = new ArrayList<Intersection>();
                }
                intermediateList.add(point);
                this.listRequestsIntersection.put(r.getPickUpPoint().getId(), intermediateList);
            }
        }
    }

    /**
     * Generate description of a pick up or delivery point for the road map
     *
     * @param id id of an intersection
     * @return text
     */
    public String writeTextForInterestPoint(long id) {
        String text = "";
        int nbPu = 0;
        int nbDe = 0;
        int puTime = 0;
        int deTime = 0;
        for (Intersection i : this.listRequestsIntersection.get(id)) {
            Intersection interestPoint = i;
            if (interestPoint instanceof PickUpPoint) {
                nbPu++;
                puTime += ((PickUpPoint) interestPoint).getPickUpDuration();
            } else if (interestPoint instanceof DeliveryPoint) {
                nbDe++;
                deTime += ((DeliveryPoint) interestPoint).getDeliveryDuration();
            }
        }
        if (nbPu>0) {
            text+= "   - You have to pick up "+nbPu+" package(s) at this intersection. This may take "+ puTime +" seconds\n\n";
        }
        if (nbDe>0) {
            text+= "   - You have to deliver "+nbDe+" package(s) at this intersection. This may take "+ deTime +" seconds\n\n";
        }
        return text;
    }

    /**
     * Generate the text for the roadmap.
     *
     * @return roadmap text
     */
    public String generateTextForRoadMap() {

        this.groupRequestIntersections();

        String totalText = "Roadmap \n\n";

        String newStreetName = "";
        String actualStreetName = "";
        int lengthTotalOnStreet = 0;
        int nbIntersections =  0;

        int i = 0;
        totalText +="   - Departure from depot at " +this.timeToString(this.listTimes.get(0)[0])+"\n\n";
        for(Path p: listPaths) {
            String PathTitle = "Step n°" + (i+1) + "\n\n";
            totalText+=PathTitle;
            int j = 0;
            for(Segment s: p.getListSegments()) {
                newStreetName = s.getStreetName();
                if ((!(newStreetName.equals(actualStreetName)))&&(!(actualStreetName.equals("")))){
                    String SegmentDescription = "   - Take "  + actualStreetName + " on " + lengthTotalOnStreet+ " m. You will cross " + nbIntersections + " intersection(s)\n\n";
                    nbIntersections =0;
                    lengthTotalOnStreet=0;
                    totalText+=SegmentDescription;
                }
                actualStreetName=newStreetName;
                lengthTotalOnStreet += (int)s.getLength();
                nbIntersections+=1;

                j++;
            }
            totalText += "   - Arrive at "+ this.timeToString(this.listTimes.get(i)[1])+"\n\n";
            totalText+=this.writeTextForInterestPoint(p.idArrival);
            i++;
        }
        totalText += "   - Arrival at depot, your tour has ended. Congratulations";
        totalText+=this.writeTextForInterestPoint(listPaths.get(i-1).idArrival);
        return totalText;
    }

    /**
     * Generate the roadmap.
     *
     * @param path text describing the path
     */
    public void generateRoadMap(String path) {
        try {
            File roadMap = new File(path);

            if (roadMap.exists()) {
                roadMap.createNewFile();
                System.out.println("File created: " + roadMap.getName());
                System.out.println("Absolute path: " + roadMap.getAbsolutePath());
                this.roadMapFilePath = roadMap.getAbsolutePath();
            } else {
                System.out.println("File already exists.");
                roadMap.delete();
                roadMap.createNewFile();
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

    /**
     * Add a path to the tour.
     *
     * @param newPath path to add
     */
    public void addPath(Path newPath) {
        listPaths.add(newPath);
        if (map.getDepot().getId() == listPaths.get(listPaths.size() - 1).getIdArrival()) {
            populateListTimes();
        }
    }

    /**
     * Convert a string describing a time into an integer.
     *
     * @param s string to convert
     * @return time
     */
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
        int minutes = Integer.parseInt(s.substring(indexEndHours + 1, indexEndMinutes));
        int time = (hours * 3600) + (minutes * 60);
        return time;
    }

    /**
     * Convert an integer describing a time into a string.
     *
     * @param t time to convert
     * @return string
     */
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

    /**
     *
     */
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
            if (stop instanceof PickUpPoint) {
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

    /**
     * @param time time to check
     * @return time
     */
    public int checkTimeUnderOneDay(int time) {
        while (time >= 86400) {
            time -= 86400;
        }
        return time;
    }
}