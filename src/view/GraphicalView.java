package view;

import javafx.scene.Node;
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

public class GraphicalView implements observer.Observer {

    MouseGestures mouseGestures;
    Map m_map;
    Pane m_overlay;
    int screenX = 1000;
    int screenY = 800;
    double zoomVal = 1.0;
    double zoomStep = 0.1;
    double zoomTranslateX = 0;
    double zoomTranslateY = 0;
    double coeffX;
    double coeffY;
    double ordonneeX;
    double ordonneeY;
    double pointSize;
    double ReqpointSize;
    double StrokeSize;

    public GraphicalView(Map map, Pane overlay, MouseGestures mg) {
        m_map = map;
        mouseGestures = mg;
        mg.newTranslateX = 0;
        mg.newTranslateY = 0;
        m_overlay = overlay;
        m_overlay.setPrefWidth(screenX);
        m_overlay.setPrefHeight(screenY);
    }

    public static List<Circle> circles = new ArrayList<Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    public static List<Arrow> arrows = new ArrayList<Arrow>();

    private void updateCoeff() {
        double maxHeigth = ((-1 * m_map.findMaxLat()) + 90) * (screenY / 180);
        double minHeigth = ((-1 * m_map.findMinLat()) + 90) * (screenY / 180);

        double maxWidth = (m_map.findMaxLong() + 180) * (screenX / 360);
        double minWidth = (m_map.findMinLong() + 180) * (screenX / 360);

        coeffX = (double) screenX / (maxWidth - minWidth) * zoomVal;
        coeffY = -(double) screenY / (maxHeigth - minHeigth) * zoomVal;

        ordonneeX = minWidth * coeffX;
        ordonneeY = maxHeigth * coeffY;

        pointSize = 0.00035 * coeffX;
        ReqpointSize = 0.0007 * coeffX;
        StrokeSize = 0.00015 * coeffX;
    }

    private void updateTranslation(double XValue, double YValue) {
        if (circles.get(1).getCenterX() < 100) {
            for (Circle circle : circles) {
                circle.setTranslateX(XValue);
                circle.setTranslateY(YValue);
            }

            for (Line line : lines) {
                line.setTranslateX(XValue);
                line.setTranslateY(YValue);

            }
            for (Arrow arrow : arrows) {
                arrow.setTranslateX(XValue);
                arrow.setTranslateY(YValue);

            }
        }
    }

    public void zoom() {
        if (zoomVal < 3.5) {
            zoomVal += zoomStep;
            updateCoeff();
            zoomTranslateX = -screenX * zoomStep;
            zoomTranslateY = -screenY * zoomStep;
            refreshMap();
            zoomTranslateX = 0;
            zoomTranslateY = 0;
        }
    }

    public void unZoom() {
        if (zoomVal > 0.5) {
            zoomVal -= zoomStep;
            updateCoeff();
            zoomTranslateX = screenX * zoomStep;
            zoomTranslateY = screenY * zoomStep;
            refreshMap();
            zoomTranslateX = 0;
            zoomTranslateY = 0;
        }
    }

    public void drawMap() {

        updateCoeff();

        //Drawing all map intersections
        for (HashMap.Entry mapentry : m_map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            if (intersection.getId() == m_map.getDepot().getId())
                drawPoint(intersection, Color.RED, pointSize * 2); //draw Depot point
            else
                drawPoint(intersection, Color.BLACK, pointSize); //draw standard point
            drawMultipleLines(intersection, intersection.getListSegments());
        }

        //draw request points
        for (Request request : m_map.getListRequests()) {

            Intersection pickup = request.getPickUpPoint();
            Intersection delivery = request.getDeliveryPoint();
            Random generator = new Random(pickup.getId());
            int rand = generator.nextInt(150);
            Color color = Color.rgb((int) (rand + 100), (int) (150 - rand + 100), (int) (75 - rand + 100));
            drawPoint(pickup, color, ReqpointSize);
            drawPoint(delivery, color, ReqpointSize);

        }

        //draw best tour
        if (!m_map.getTour().getListPaths().isEmpty() && m_map.getTour() != null) {
            System.out.println("ENTREE");
            for (Path path : m_map.getTour().getListPaths()) {
                Intersection depart = m_map.getListIntersections().get(path.getIdDeparture());
                if (depart != null) {
                    for (Segment segment : path.getListSegments()) {
                        Intersection step = m_map.getListIntersections().get(segment.getDestination());
                        drawLine(depart, step, StrokeSize * 1.5);
                        depart = step;
                    }
                }
            }
        }

        //allow for objects to be moved
        mouseGestures.makeMovable(m_overlay, circles, lines, arrows);

        updateTranslation(mouseGestures.newTranslateX + zoomTranslateX, mouseGestures.newTranslateY + zoomTranslateY);
        mouseGestures.newTranslateX += zoomTranslateX / 2;
        mouseGestures.newTranslateY += zoomTranslateY / 2;

        for (Line line : lines) {
            m_overlay.getChildren().add(line);
        }

        for (Circle circle : circles) {
            m_overlay.getChildren().add(circle);
        }

        for (Arrow arrow : arrows) {
            m_overlay.getChildren().add(arrow);
        }

    }

    public void enableSelection() {
        for (Node node : m_overlay.getChildren()) {
            if (node instanceof Circle)
                mouseGestures.makeClickable(node);
        }
    }

    public void refreshMap() {
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
        arrows.clear();
        drawMap();
    }

    public void disableSelection() {
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
        arrows.clear();
        drawMap();
    }


    private void drawMultipleLines(Intersection origin, List<Segment> segmentList) {

        double originX = longToPix(origin.getLongitude());
        double originY = latToPix(origin.getLatitude());

        for (Segment segment : segmentList) {
            Intersection destination = m_map.getListIntersections().get(segment.getDestination());
            double destinationX = longToPix(destination.getLongitude());
            double destinationY = latToPix(destination.getLatitude());
            Line line = new Line(originX, originY, destinationX, destinationY);
            line.setStrokeWidth(StrokeSize);
            lines.add(line);
        }
    }

    private void drawLine(Intersection origin, Intersection destination, double size) {

        double originX = longToPix(origin.getLongitude());
        double originY = latToPix(origin.getLatitude());
        double destinationX = longToPix(destination.getLongitude());
        double destinationY = latToPix(destination.getLatitude());
        Arrow arrow = new Arrow(originX, originY, destinationX, destinationY, pointSize);
        arrow.setFill(Color.RED);
        arrow.setStrokeWidth(StrokeSize);
        arrows.add(arrow);
    }

    private void drawPoint(Intersection intersection, Color color, double size) {

        double pointX = longToPix(intersection.getLongitude());
        double pickupY = latToPix(intersection.getLatitude());

        Circle circle = new Circle(size);
        circle.setStroke(Color.BLACK);
        circle.setFill(color.deriveColor(1, 1, 1, 1.0));
        circle.relocate(pointX - size, pickupY - size);
        circle.setRotate(0.0);
        circle.setUserData(intersection.getId());
        circles.add(circle);
    }

    public List<Intersection> getSelectedIntersection() {
        List<Intersection> listIntersections = new ArrayList<Intersection>();
        for (Circle circle : circles) {
            if (circle.getRotate() == 1.0)
                listIntersections.add(m_map.getListIntersections().get(circle.getUserData()));

        }
        return listIntersections;
    }

    private double latToPix(double lat) {
        return ((-1 * lat) + 90) * (screenY / 180) * coeffY - ordonneeY;
    }

    private double longToPix(double lon) {
        return (lon + 180) * (screenX / 360) * coeffX - ordonneeX;
    }

    @Override
    public void update(observer.Observable observed, Object arg) {
        refreshMap();
    }
}