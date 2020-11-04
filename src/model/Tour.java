package model;

import controler.ComputeSmallestPath;

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

    /**
     * Default constructor
     */
    public Tour() {
        this.listPaths = new LinkedList<Path>();
        this.tourLength = 0;
    }

    public Tour(List<Path> listPaths) {
        this.listPaths = listPaths;
        this.tourLength = 0;
        for(Path p: listPaths) {
            this.tourLength += p.getPathLength();
        }
    }

    public void addRequestToTour(Request newRequest,Long precedingPickUpId,Long precedingDeliveryId,Map m) {
        ComputeSmallestPath calculator = new ComputeSmallestPath(m);
        int pathIndexToInsertPickUp = 0;
        int pathIndexToInsertDelivery = 0;
        for (Path path :listPaths) {
            pathIndexToInsertPickUp++;
            if (path.getIdDeparture() == precedingPickUpId) {
                List<Segment> roadDeparturetoNewPickUp =
                        calculator.computeSmallestPath(m.getListIntersections().get(path.getIdDeparture()),newRequest.getPickUpPoint());
                List<Segment> roadNewPickUptoArrival =
                        calculator.computeSmallestPath(newRequest.getPickUpPoint(),m.getListIntersections().get(path.getIdArrival()));
                Path pathDeparturetoNewPickUp = new Path (roadDeparturetoNewPickUp,path.getIdDeparture(),newRequest.getPickUpPoint().getId());
                Path pathNewPickUptoArrival = new Path (roadNewPickUptoArrival,newRequest.getPickUpPoint().getId(),path.getIdArrival());
                listPaths.remove(pathIndexToInsertPickUp);
                listPaths.add(pathIndexToInsertPickUp,pathDeparturetoNewPickUp);
                listPaths.add(pathIndexToInsertPickUp+1,pathNewPickUptoArrival);
                break;
            }
        }
        for (Path path :listPaths) {
            pathIndexToInsertDelivery++;
            if (path.getIdDeparture() == precedingDeliveryId) {
                List<Segment> roadDeparturetoNewDelivery =
                        calculator.computeSmallestPath(m.getListIntersections().get(path.getIdDeparture()),newRequest.getDeliveryPoint());
                List<Segment> roadNewDeliverytoArrival =
                        calculator.computeSmallestPath(newRequest.getDeliveryPoint(),m.getListIntersections().get(path.getIdArrival()));
                Path pathDeparturetoNewDelivery = new Path (roadDeparturetoNewDelivery,path.getIdDeparture(),newRequest.getDeliveryPoint().getId());
                Path pathNewDeliverytoArrival = new Path (roadNewDeliverytoArrival,newRequest.getDeliveryPoint().getId(),path.getIdArrival());
                listPaths.remove(pathIndexToInsertDelivery);
                listPaths.add(pathIndexToInsertDelivery,pathDeparturetoNewDelivery);
                listPaths.add(pathIndexToInsertDelivery+1,pathNewDeliverytoArrival);
                break;
            }
        }
    }

    public void removeRequestFromTour(Request request) {
        for (Path path : listPaths) {
            if (path.getIdArrival() == request.getPickUpPoint().getId()) {

            }
        }
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

    public void addPath(Path newPath) {
        listPaths.add(newPath);
        tourLength += newPath.pathLength;
    }
}