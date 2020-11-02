package view;

import com.sun.source.tree.IntersectionTypeTree;
import controler.XMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    MouseGestures m_mg;
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

    public GraphicalView(Map map,Pane overlay, MouseGestures mg) {
        m_map = map;
        m_mg = mg;
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

    public void zoom() {
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

        //Drawing all map intersections
        for (HashMap.Entry mapentry : m_map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if(intersection.getId() == m_map.getDepot().getId())
                drawPoint(intersection, Color.RED, pointSize*2); //draw Depot point
            else
                drawPoint(intersection, Color.BLACK, pointSize); //draw standard point
            drawMultipleLines(intersection, intersection.getListSegments());
        }

        //draw request points
        for (Request request : m_map.getListRequests()) {

            Intersection pickup = request.getPickUpPoint();
            Intersection delivery = request.getDeliveryPoint();

            drawPoint(pickup, Color.BLUE, ReqpointSize);
            drawPoint(delivery, Color.YELLOW, ReqpointSize);

        }

            //draw best tour
            if(m_map.getTour() != null) {
                System.out.println("ENTREE");
                for (Path path: m_map.getTour().getListPaths()) {
                      Intersection depart = m_map.getListIntersections().get(path.getIdDeparture());
                    for (Segment segment:path.getListSegments()) {
                        Intersection step = m_map.getListIntersections().get(segment.getDestination());
                        drawLine(depart,step,StrokeSize*1.5);
                        depart = step;
                    }
                }
            }

        //allow for objects to be moved
        m_mg.makeMovable(m_overlay, circles, lines);

        for (Line line:lines) {
            m_overlay.getChildren().add(line);
        }

        for (Circle circle:circles) {
            m_overlay.getChildren().add(circle);
        }

    }

    public void enableSelection() {
        for (Node node:m_overlay.getChildren()) {
            if(node instanceof Circle)
                m_mg.makeClickable(node);
        }
    }

    public void refreshMap(){
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
        drawMap();
    }

    public void clearMap(){
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
    }

    private void drawMultipleLines(Intersection origin, List<Segment> segmentList) {

        double originX = (origin.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double originY = ((-1 * origin.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;

        for (Segment segment : segmentList) {
            Intersection destination = m_map.getListIntersections().get(segment.getDestination());
            double destinationX = (destination.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
            double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
            Line line = new Line(originX, originY, destinationX, destinationY);
            line.setStrokeWidth(StrokeSize);
            lines.add(line);
        }
    }

    private void drawLine(Intersection origin, Intersection destination, double size) {

        double originX = (origin.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double originY = ((-1 * origin.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
        double destinationX = (destination.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
        double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
        Line line = new Line(originX, originY, destinationX, destinationY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(size);
        lines.add(line);
    }

    private void drawPoint(Intersection intersection, Color color, double size) {

        double pointX = (intersection.getLongitude() + 180) * (screenX / 360)*coeffX - ordonneeX;
        double pickupY = ((-1 * intersection.getLatitude()) + 90) * (screenY / 180)*coeffY - ordonneeY;

        Circle circle = new Circle(size);
        circle.setStroke(Color.BLACK);
        circle.setFill(color.deriveColor(1, 1, 1, 0.9));
        circle.relocate(pointX - size, pickupY - size);
        circle.setRotate(0.0);
        circle.setUserData(intersection.getId());
        circles.add(circle);
    }

    public List<Intersection> getSelectedIntersection() {
        List<Intersection> listIntersections = new ArrayList<Intersection>();
        for (Circle circle: circles) {
            if(circle.getRotate()==1.0)
                listIntersections.add(m_map.getListIntersections().get(circle.getUserData()));

        }
        return listIntersections;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}