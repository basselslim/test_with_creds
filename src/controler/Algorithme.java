package controler;
import model.*;
import model.Map;

import java.util.*;

public class Algorithme {

    protected Map map;
    protected List<Request> listRequests;
    protected final long timeZero;
    protected final long TIMEOUT = 20000;

    public Algorithme(Map map) {
        this.map = map;
        this.listRequests = map.getListRequests();
        this.timeZero = System.currentTimeMillis();
    }

    /*
     * Compute smallest path
     */
    public HashMap<Long, HashMap<Long,Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        ComputeSmallestPath algorithmSmallestPath = new ComputeSmallestPath(this.map);
        HashMap<Long, HashMap<Long,Path>> mapSmallestPaths = new HashMap<>();

        List<Intersection> listPoints = new ArrayList<>();
        Intersection point;

        point = map.getListIntersections().get(map.getDepot().getId());
        if (point != null) {
            listPoints.add(point);
        }

        for (Request r: this.listRequests) {
            point = map.getListIntersections().get(r.getDeliveryPoint().getId());
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
            point = map.getListIntersections().get(r.getPickUpPoint().getId());
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
        }

        for (Intersection p1: listPoints) {
            HashMap<Long,Path> mapSmallestPathPerPoint = new HashMap<>();
            for (Intersection p2: listPoints) {
                if (p1.getId() != p2.getId()) {
                    List<Intersection> listIntersections = algorithmSmallestPath.computeSmallestPath(p1, p2);

                    List<Segment> listSegments = new ArrayList<>();
                    Intersection step = p1;

                    for (Intersection i: listIntersections) {
                        if (i.getId() != step.getId()) {
                            for (Segment s: step.getListSegments()) {
                                if (s.getDestination() == i.getId()) {
                                    listSegments.add(s);
                                }
                            }
                        }
                        step = i;
                    }
                    mapSmallestPathPerPoint.put(p2.getId(), new Path(listSegments, p1.getId(), p2.getId()));
                }
            }
            mapSmallestPaths.put(p1.getId(), mapSmallestPathPerPoint);
        }
        System.out.println("Smallest paths computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
        return mapSmallestPaths;
    }

    /*
     * Compute optimal tour
     */
    public LinkedList<Path> computeOptimalTour(HashMap<Long, List<Path>> mapSmallestPaths) {
        System.out.println("Computing the optimal tour...");
        LinkedList<Path> optimalTour = new LinkedList<>();
        while (System.currentTimeMillis() - this.timeZero < this.TIMEOUT) {
            /*
             * Algo
             */
        }
        System.out.println("Optimal tour computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
        return optimalTour;
    }
}
