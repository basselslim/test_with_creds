package controler;
import model.Intersection;
import model.Map;
import model.Path;
import model.Request;

import java.util.*;

public class Algorithme {

    protected Map map;
    protected List<Request> listRequests;
    protected final long timeZero;
    protected final long TIMEOUT = 20000;

    public Algorithme(Map map, List<Request> listRequests) {
        this.map = map;
        this.listRequests = listRequests;
        this.timeZero = System.currentTimeMillis();

        HashMap<Long, List<Path>> mapSmallestPaths = this.computeSmallestPaths();
        LinkedList<Path> optimalTour = this.computeOptimalTour(mapSmallestPaths);
    }

    /*
     * Compute smallest path
     */
    public HashMap<Long, List<Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        HashMap<Long, List<Path>> mapSmallestPaths = new HashMap<>();

        Queue<Intersection> openSet = new PriorityQueue<>();
        while (System.currentTimeMillis() - this.timeZero < this.TIMEOUT) {
            /*
             * Algo
             */
        }
        System.out.println("Smallest paths computed in " + (System.currentTimeMillis() - this.timeZero)/1000.0 + "s.");
        return mapSmallestPaths;
    }

    public double computeCost(Intersection from, Intersection to) {
        double R = 6372.8; // Earth's Radius, in kilometers

        double dLat = Math.toRadians(to.getLatitude() - from.getLatitude());
        double dLon = Math.toRadians(to.getLongitude() - from.getLongitude());
        double lat1 = Math.toRadians(from.getLatitude());
        double lat2 = Math.toRadians(to.getLatitude());

        double a = Math.pow(Math.sin(dLat / 2),2)
                + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
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
