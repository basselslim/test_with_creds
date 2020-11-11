package controler;
import model.*;
import model.Map;
import java.util.*;

/**
 * Algorithm that compute the optimal tour.
 *
 * @author T-REXANOME
 */
public class Algorithm {

    protected Map map;
    protected List<Request> listRequests;
    protected long timeZero;
    protected long TIMELIMIT = 20000;

    /**
     * Constructor.
     *
     * @param map map object
     */
    public Algorithm(Map map) {
        this.map = map;
        this.listRequests = map.getListRequests();
    }

    /**
     * Compute smallest path.
     *
     * @return smallest path
     */
    public HashMap<Long, HashMap<Long,Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        this.timeZero = System.currentTimeMillis();
        ComputeSmallestPath algorithmSmallestPath = new ComputeSmallestPath(this.map);
        HashMap<Long, HashMap<Long,Path>> mapSmallestPaths = new HashMap<>();

        List<Step> listPoints = new ArrayList<>();
        Step point;

        point = map.getDepot();
        if (point != null) {
            listPoints.add(point);
        }

        for (Request r: this.listRequests) {
            point = r.getDeliveryPoint();
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
            point = r.getPickUpPoint();
            if (!listPoints.contains(point) && point != null) {
                listPoints.add(point);
            }
        }

        for (Step p1: listPoints) {
            HashMap<Long,Path> mapSmallestPathPerPoint = new HashMap<>();
            for (Step p2: listPoints) {
                if (p1.getId() != p2.getId()) {
                    List<Segment> listSegments = algorithmSmallestPath.computeSmallestPath(p1, p2);
                    mapSmallestPathPerPoint.put(p2.getId(), new Path(listSegments, p1, p2));
                }
            }
            mapSmallestPaths.put(p1.getId(), mapSmallestPathPerPoint);
        }
        System.out.println("Smallest paths computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
        return mapSmallestPaths;
    }

    /**
     * Compute optimal tour.
     *
     * @param mapSmallestPaths optimal tour
     */
    public void computeOptimalTour(HashMap<Long, HashMap<Long,Path>> mapSmallestPaths) {
        this.timeZero = System.currentTimeMillis();
        System.out.println("Computing the optimal tour...");
        TravellingSalesmanProblem travellingSalesmanProblem = new TravellingSalesmanProblem(map,mapSmallestPaths,TIMELIMIT);
        travellingSalesmanProblem.TSP();
        System.out.println("Optimal tour computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
    }
}
