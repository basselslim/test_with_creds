package controler;
import model.*;
import model.Map;
import java.util.*;

public class Algorithm {

    protected Map map;
    protected List<Request> listRequests;
    protected long timeZero;
    protected long TIMELIMIT = 20000;
    public Algorithm(Map map) {
        this.map = map;
        this.listRequests = map.getListRequests();
    }

    /*
     * Compute smallest path
     */
    public HashMap<Long, HashMap<Long,Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        this.timeZero = System.currentTimeMillis();
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
                    List<Segment> listSegments = algorithmSmallestPath.computeSmallestPath(p1, p2);
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
    public void computeOptimalTour(HashMap<Long, HashMap<Long,Path>> mapSmallestPaths) {
        this.timeZero = System.currentTimeMillis();
        System.out.println("Computing the optimal tour...");
        TravellingSalesmanProblem travellingSalesmanProblem = new TravellingSalesmanProblem(map,mapSmallestPaths,TIMELIMIT);
        travellingSalesmanProblem.TSP();
        System.out.println("Optimal tour computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
    }
    
    public void connectToDb() {
        String username = 'user_1KI8';
        String password = 'NJIZ90Z2IHZG';
    }
}
