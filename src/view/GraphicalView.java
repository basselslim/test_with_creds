package view;

import com.sun.source.tree.IntersectionTypeTree;
import controler.XMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.*;
import model.Map;

import java.util.*;

/**
 * 
 */
public class GraphicalView implements Observer {

    Map m_map;
    Pane m_overlay;
    int screenX = 1200;
    int screenY = 600;
    double zoomVal = 1.0;
    double coeffX = (double)screenX/(554.64-554.57)*zoomVal;
    double coeffY = (double)screenY/(132.76-132.71)*zoomVal;
    double ordonneeX = 554.57*coeffX;
    double ordonneeY = 132.71*coeffY;
    double pointSize = 8.0*zoomVal;
    double ReqpointSize = 15.0*zoomVal;
    double StrokeSize = 4.0*zoomVal;

    public GraphicalView(Map map,Pane overlay) {
        m_map = map;

        m_overlay = overlay;
        m_overlay.setPrefWidth(screenX);
        m_overlay.setPrefHeight(screenY);
    }

    public static List<Circle> circles = new ArrayList<Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    private void updateCoeff() {
        double maxHeigth = ((-1 * m_map.findMaxLat()) + 90) * (screenY / 180);
        double minHeigth = ((-1 * m_map.findMinLat()) + 90) * (screenY / 180);

        double maxWidth = (m_map.findMaxLong() + 180) * (screenX / 360);
        double minWidth = (m_map.findMinLong() + 180) * (screenX / 360);

        coeffX = (double)screenX/(maxWidth-minWidth)*zoomVal;
        coeffY = (double)screenY/(maxHeigth-minHeigth)*zoomVal;

        ordonneeX = minWidth*coeffX;
        ordonneeY = minHeigth*coeffY;

        pointSize = 0.0004*coeffX;
        ReqpointSize = 0.0008*coeffX;
        StrokeSize = 0.0002*coeffX;
    }

    public void Zoom() {
        if(zoomVal <3.0)
            zoomVal +=0.1;

        updateCoeff();
        refreshMap();

    }

    public void unZoom() {
        if(zoomVal > 0.5)
            zoomVal -=0.1;

        updateCoeff();
        refreshMap();

    }

    public void drawMap() {

        updateCoeff();

        for (HashMap.Entry mapentry : m_map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            double originX = (intersection.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
            double originY = ((-1 * intersection.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;

            Circle circle = new Circle(pointSize);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
            circle.relocate(originX - pointSize, originY - pointSize);
            circles.add(circle);

            drawMultipleLines(intersection,intersection.getListSegments());
        }
            for (Request request:m_map.getListRequests()) {

                Intersection pickup = request.getPickUpPoint();
                Intersection delivery = request.getDeliveryPoint();

                double pickupX = (pickup.getLongitude() + 180) * (screenX / 360)*coeffX - ordonneeX;
                double pickupY = ((-1 * pickup.getLatitude()) + 90) * (screenY / 180)*coeffY - ordonneeY;
                double deliveryX = (delivery.getLongitude() + 180) * (screenX / 360)*coeffX - ordonneeX;
                double deliveryY = ((-1 * delivery.getLatitude()) + 90) * (screenY / 180)*coeffY - ordonneeY;

                Circle pickupCircle = new Circle(ReqpointSize);
                pickupCircle.setStroke(Color.BLACK);
                pickupCircle.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.9));
                pickupCircle.relocate(pickupX-ReqpointSize, pickupY-ReqpointSize);
                circles.add(pickupCircle);

                Circle deliveryCircle = new Circle(ReqpointSize);
                deliveryCircle.setStroke(Color.BLACK);
                deliveryCircle.setFill(Color.GREEN.deriveColor(1, 1, 1, 0.9));
                deliveryCircle.relocate(deliveryX-ReqpointSize, deliveryY-ReqpointSize);
                circles.add(deliveryCircle);
            }

            if(m_map.getMapSmallestPaths() != null) {
                System.out.println("ENTREE");
            for (HashMap.Entry mapentry : m_map.getMapSmallestPaths().entrySet()) {
                List<Path> ListPaths = (List<Path>) mapentry.getValue();
                for (Path path: ListPaths) {
                      Intersection depart = m_map.getListIntersections().get(path.getIdDeparture());
                    for (Segment segment:path.getListSegments()) {
                        Intersection step = m_map.getListIntersections().get(segment.getDestination());
                        drawLine(depart,step);
                        depart = step;
                    }
                }
            }
        }

        MouseGestures mg = new MouseGestures();
        mg.makeMovable(m_overlay, circles, lines);

        for (Line line:lines) {
            line.setStrokeWidth(StrokeSize);
            m_overlay.getChildren().add(line);
        }

        for (Circle circle:circles) {
            mg.makeClickable(circle);
            m_overlay.getChildren().add(circle);
        }
    }

    public void refreshMap(){
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
        drawMap();
    }

    public void clear(){
        m_overlay.getChildren().clear();
    }

    public void drawMultipleLines(Intersection origin, List<Segment> Listsegment) {

        double originX = (origin.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double originY = ((-1 * origin.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;

        for (Segment segment : Listsegment) {
            Intersection destination = m_map.getListIntersections().get(segment.getDestination());
            double destinationX = (destination.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
            double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
            Line line = new Line(originX, originY, destinationX, destinationY);
            lines.add(line);
        }
    }

    public void drawLine(Intersection origin, Intersection destination) {

        double originX = (origin.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double originY = ((-1 * origin.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
        double destinationX = (destination.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
        Line line = new Line(originX, originY, destinationX, destinationY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(10.0);
        lines.add(line);
    }

    @Override
    public void update(Observable o, Object arg) {

    }


}