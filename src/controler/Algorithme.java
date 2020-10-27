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


        // LinkedList<Path> optimalTour = this.computeOptimalTour(mapSmallestPaths);
    }

    /*
     * Compute smallest path
     */
    public HashMap<Long, List<Path>> computeSmallestPaths() {
        System.out.println("Computing the smallest paths...");
        ComputeSmallestPath algorithm = new ComputeSmallestPath(this.map);
        HashMap<Long, List<Path>> mapSmallestPaths = new HashMap<>();

        // while (System.currentTimeMillis() - this.timeZero < this.TIMEOUT) {
            List<Intersection> listPoints = new ArrayList<>();
            for (Request r: this.listRequests) {
                listPoints.add(map.getListIntersections().get(r.getDeliveryPoint().getId()));
                listPoints.add(map.getListIntersections().get(r.getPickUpPoint().getId()));
            }

            for (Intersection p1: listPoints) {
                List<Path> listPaths = new ArrayList<>();
                for (Intersection p2: listPoints) {
                    if (p1.getId() != p2.getId()) {
                        List<Intersection> listIntersections = algorithm.computeSmallestPath(p1, p2);

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
                        listPaths.add(new Path(listSegments, p1.getId(), p2.getId()));
                    }
                }
                mapSmallestPaths.put(p1.getId(), listPaths);
            }
        // }
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
