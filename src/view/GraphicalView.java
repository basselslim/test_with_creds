package view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
    int screenX;
    int screenY;
    double zoomVal = 1.0;
    double zoomStep = 0.2;
    double zoomTranslateX = 0;
    double zoomTranslateY = 0;
    double coeffX;
    double coeffY;
    double ordonneeX;
    double ordonneeY;
    double pointSize;
    double ReqpointSize;
    double StrokeSize;
    Boolean isMapClickable = false;
    Boolean setScreenSize = false;

    public GraphicalView(Map map, Pane overlay, MouseGestures mg, Rectangle2D screenBounds) {
        m_map = map;
        mouseGestures = mg;
        mouseGestures.setGview(this);
        mg.newTranslateX = 0;
        mg.newTranslateY = 0;
        m_overlay = overlay;
        screenX = (int)((screenBounds.getMaxX()-100)*0.595);
        screenY = (int)((screenBounds.getMaxY()-100)*0.762);
    }

    public static HashMap<Long,Circle> circles = new HashMap<Long,Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    public static List<Arrow> arrows = new ArrayList<Arrow>();

    public static HashMap<Long,Rectangle> rectangles = new HashMap<Long,Rectangle>();

    public void drawMap() {
        if (!setScreenSize) {
            m_overlay.setPrefWidth(screenX);
            m_overlay.setPrefHeight(screenY);
            setScreenSize = true;
        }
        updateCoeff();

        //Drawing all map intersections
        for (HashMap.Entry mapentry : m_map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            drawPoint(intersection, Color.BLACK, pointSize,false); //draw standard point
            drawMultipleLines(intersection, intersection.getListSegments());
        }

        //draw request points
        for (Request request : m_map.getListRequests()) {

            Intersection pickup = request.getPickUpPoint();
            Intersection delivery = request.getDeliveryPoint();
            Random generator = new Random(pickup.getId());
            int rand = generator.nextInt(150);
            int rand2 = generator.nextInt(150);
            int rand3 = generator.nextInt(150);
            Color color = Color.rgb(rand + 100,(rand2 + 100),(rand3 + 100));
            drawRectangle(pickup, color, ReqpointSize);
            drawPoint(delivery, color, ReqpointSize,true);

        }

        //draw best tour
        if (!m_map.getTour().getListPaths().isEmpty() && m_map.getTour() != null) {
            System.out.println("ENTREE");
            for (Path path : m_map.getTour().getListPaths()) {
                Intersection depart = m_map.getListIntersections().get(path.getIdDeparture());
                if (depart != null) {
                    for (Segment segment : path.getListSegments()) {
                        Intersection step = m_map.getListIntersections().get(segment.getDestination());
                        drawArrow(depart, step, pointSize);
                        depart = step;
                    }
                }
            }
        }

        //allow for objects to be moved
        mouseGestures.makeMovable(m_overlay, circles, lines, arrows, rectangles);

        updateTranslation(mouseGestures.newTranslateX + zoomTranslateX, mouseGestures.newTranslateY + zoomTranslateY);

        addNodesToOverlay();

        if(isMapClickable = true)
            enableSelection();

    }

    public void refreshMap() {
        m_overlay.getChildren().clear();
        lines.clear();
        circles.clear();
        arrows.clear();
        rectangles.clear();
        drawMap();
    }

    public void enableSelection() {
        isMapClickable = true;
        for (Node node : m_overlay.getChildren()) {
            if (node instanceof Circle || node instanceof Rectangle)
                mouseGestures.makeClickable(node);
        }
    }

    public void disableSelection() {
        isMapClickable = false;
        refreshMap();
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
        double width = latToPix(m_map.getMinLat())-latToPix(m_map.getMaxLat());
        if (width > screenY) {
            zoomVal -= zoomStep;
            updateCoeff();
            zoomTranslateX = screenX * zoomStep;
            zoomTranslateY = screenY * zoomStep;
            refreshMap();
            zoomTranslateX = 0;
            zoomTranslateY = 0;
        }
    }

    public void drawMouseSelection(long NodeId) {
        Circle circle = circles.get(NodeId);
        Rectangle rectangle = rectangles.get(NodeId);
        if (circle != null) {
            //circle.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));
            circle.setStrokeWidth(circle.getStrokeWidth() * 1.5);
            circle.setStroke(Color.RED);
        }
        if (rectangle != null) {
            //rectangle.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));
            rectangle.setStrokeWidth(rectangle.getStrokeWidth() * 1.5);
            rectangle.setStroke(Color.RED);
        }

    }

    public void undrawMouseSelection(long NodeId) {
        Circle circle = circles.get(NodeId);
        Rectangle rectangle = rectangles.get(NodeId);
        if (circle != null) {
            //circle.setFill(Color.BLACK);
            circle.setStrokeWidth(circle.getStrokeWidth() / 1.5);
            circle.setStroke(Color.BLACK);
        }
        if (rectangle != null) {
            //rectangle.setFill(Color.BLACK);
            rectangle.setStrokeWidth(rectangle.getStrokeWidth() / 1.5);
            rectangle.setStroke(Color.BLACK);
        }
    }


    private void drawMultipleLines(Intersection origin, List<Segment> segmentList) {

        double originX = longToPix(origin.getLongitude());
        double originY = latToPix(origin.getLatitude());

        for (Segment segment : segmentList) {
            Intersection destination = m_map.getListIntersections().get(segment.getDestination());
            double destinationX = longToPix(destination.getLongitude());
            double destinationY = latToPix(destination.getLatitude());
            double length = Math.sqrt(Math.pow(destinationX-originX,2)+Math.pow(destinationY-originY,2));
            Line line = new Line(originX, originY, destinationX, destinationY);
            line.setStrokeWidth(StrokeSize);
            lines.add(line);
        }
    }

    private void drawArrow(Intersection origin, Intersection destination, double size) {

        double originX = longToPix(origin.getLongitude());
        double originY = latToPix(origin.getLatitude());
        double destinationX = longToPix(destination.getLongitude());
        double destinationY = latToPix(destination.getLatitude());
        Arrow arrow = new Arrow(originX, originY, destinationX, destinationY, size);
        arrow.setFill(Color.RED);
        arrow.setStrokeWidth(StrokeSize);
        arrows.add(arrow);
    }

    private void drawPoint(Intersection intersection, Color color, double size, Boolean isRequest) {

        double pointX = longToPix(intersection.getLongitude());
        double pickupY = latToPix(intersection.getLatitude());

        Circle circle = new Circle(size);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(StrokeSize);
        circle.setFill(color.deriveColor(1, 1, 1, 1.0));
        circle.relocate(pointX - size, pickupY - size);
        circle.setUserData(intersection.getId());
        if(isRequest)
            circle.setViewOrder(-1.0);
        circles.put(intersection.getId(),circle);
    }

    private void drawRectangle(Intersection intersection, Color color, double size) {

        double pointX = longToPix(intersection.getLongitude());
        double pickupY = latToPix(intersection.getLatitude());

        Rectangle rectangle = new Rectangle(pointX-size,pickupY-size, size*2, size*2);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(StrokeSize);
        rectangle.setFill(color.deriveColor(1, 1, 1, 1.0));
        rectangle.setUserData(intersection.getId());
        circles.remove(intersection.getId());
        rectangles.put(intersection.getId(),rectangle);
    }

    private void addNodesToOverlay(){
        for (Line line : lines) {
            m_overlay.getChildren().add(line);
        }

        Circle depot = new Circle();
        for (HashMap.Entry mapentry : circles.entrySet()) {
            Circle circle = (Circle)mapentry.getValue();
            if ((long)mapentry.getKey() == m_map.getDepot().getId()) {
                depot = circle;
                depot.setRadius(pointSize * 2);
                depot.setFill(Color.RED);
                depot.setStroke(Color.BLACK);
                depot.setStrokeWidth(StrokeSize * 1.5);
                depot.setViewOrder(-1.0);
            } else
                m_overlay.getChildren().add(circle);
        }
        m_overlay.getChildren().add(depot);

        for (Arrow arrow : arrows) {
            m_overlay.getChildren().add(arrow);
        }

        for (HashMap.Entry mapentry : rectangles.entrySet()) {
            Rectangle rectangle = (Rectangle) mapentry.getValue();
            m_overlay.getChildren().add(rectangle);
        }
    }

    private void updateCoeff() {
        double maxHeigth = ((-1 * m_map.getMaxLat()) + 90) * (screenY / 180);
        double minHeigth = ((-1 * m_map.getMinLat()) + 90) * (screenY / 180);

        double maxWidth = (m_map.getMaxLong() + 180) * (screenX / 360);
        double minWidth = (m_map.getMinLong() + 180) * (screenX / 360);

        coeffX = (double) screenX / (maxWidth - minWidth) * zoomVal;
        coeffY = -(double) screenY / (maxHeigth - minHeigth) * zoomVal;

        ordonneeX = minWidth * coeffX;
        ordonneeY = maxHeigth * coeffY;

        pointSize = 0.00030 * coeffX;
        ReqpointSize = 0.0007 * coeffX;
        StrokeSize = 0.00016 * coeffX;
    }

    private void updateTranslation(double XValue, double YValue) {

        for (HashMap.Entry mapentry : circles.entrySet()) {
            Circle circle = (Circle) mapentry.getValue();
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

        for (HashMap.Entry mapentry : rectangles.entrySet()) {
            Rectangle rectangle = (Rectangle) mapentry.getValue();
            rectangle.setTranslateX(XValue);
            rectangle.setTranslateY(YValue);
        }
        mouseGestures.newTranslateX += zoomTranslateX / 2;
        mouseGestures.newTranslateY += zoomTranslateY / 2;
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