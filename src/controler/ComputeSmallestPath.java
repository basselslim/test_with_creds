package controler;

import model.Intersection;
import model.Map;
import model.Segment;

import java.util.*;

public class ComputeSmallestPath {
    protected Map map;

    public ComputeSmallestPath(Map map) {
        this.map = map;
    }

    public List<Intersection> computeSmallestPath(Intersection from, Intersection to) {
        List<Intersection> computedPath;
        Queue<Intersection> openSet = new PriorityQueue<>();
        Intersection start = new Intersection(from, null, 0d, computeCost(from, to));
        openSet.add(start);

        HashMap<Long, Intersection> closedMap = new HashMap<>();

        while (!openSet.isEmpty()) {
            Intersection next = openSet.poll();
            if (next.getCurrent().equals(to)) {
                computedPath = new ArrayList<>();
                Intersection current = next;
                do {
                    computedPath.add(0, current.getCurrent());
                    current = closedMap.get(current.getPrevious());
                } while (current != null);
                return computedPath;
            }
            List<Segment> listNeighbours = next.getListSegments();
            for (Segment s: listNeighbours) {
                long idNextNode = s.getDestination();
                Intersection neighbour = this.map.getListIntersections().get(idNextNode);

                double newScore = next.getRouteScore() + computeCost(next.getCurrent(), next);
                if (!(closedMap.containsKey(neighbour.getId()) || (openSet.contains(neighbour) &&  neighbour.getRouteScore() < newScore))) {
                    neighbour.setPrevious(next.getCurrent());
                    neighbour.setRouteScore(newScore);
                    neighbour.setEstimatedScore(newScore + computeCost(next, to));
                    openSet.add(neighbour);
                }
            }
            closedMap.put(next.getId(), next);
        }
        return null;
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
}
