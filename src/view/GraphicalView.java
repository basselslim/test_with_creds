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
import model.Intersection;
import model.Map;
import model.Request;
import model.Segment;

import java.util.*;

/**
 * 
 */
public class GraphicalView implements Observer {

    Map m_map;
    Pane m_overlay;
    Canvas m_canvas;
    int screenX = 1200;
    int screenY = 600;
    double zoomVal = 0.4;

    public GraphicalView(Map map,Canvas canvas,Pane overlay) {
        m_map = map;
        m_canvas = canvas;
        m_overlay = overlay;
        m_canvas.setWidth(screenX);
        m_canvas.setHeight(screenY);
        m_overlay.setPrefWidth(screenX);
        m_overlay.setPrefHeight(screenY);
    }

    public static List<Circle> circles = new ArrayList<Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    public void drawShapes(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, screenX, screenY, 20, 20);
    }

    public void zoom() {
        zoomVal+= 1.0;
        m_overlay.getChildren().clear();
        drawMap();
    }



    public void drawMap() {

        double coeffX = (double)screenX/(554.64-554.57)*zoomVal;
        double coeffY = (double)screenY/(132.76-132.71)*zoomVal;
        double ordonneeX = 554.57*coeffX;
        double ordonneeY = 132.71*coeffY;
        double pointSize = 5.0*zoomVal;
        double ReqpointSize = 15.0*zoomVal;

        for (HashMap.Entry mapentry : m_map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            double originX = (intersection.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
            double originY = ((-1 * intersection.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;

            Circle circle = new Circle(pointSize);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
            circle.relocate(originX - pointSize, originY - pointSize);
            circles.add(circle);

            for (Segment segment : intersection.getListSegments()) {
                Intersection destination = m_map.getListIntersections().get(segment.getDestination());
                double destinationX = (destination.getLongitude() + 180) * (screenX / 360) * coeffX - ordonneeX;
                double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180) * coeffY - ordonneeY;
                Line line = new Line(originX, originY, destinationX, destinationY);
                lines.add(line);
            }
        }
            System.out.println("TAILLE LISTE = "+m_map.getListRequests().size());
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


        MouseGestures mg = new MouseGestures();
        mg.makeMovable(m_overlay, circles, lines);

        for (Line line:lines) {
            line.setStrokeWidth(4*zoomVal);
            m_overlay.getChildren().add(line);
        }

        for (Circle circle:circles) {
            mg.makeClickable(circle);
            m_overlay.getChildren().add(circle);
        }
    }

    public void drawRequests() {
        m_overlay.getChildren().clear();
        drawMap();
    }

    public void reloadMap(){
       //m_overlay.getChildren().clear();
        m_overlay = new Pane();

        //m_canvas.getGraphicsContext2D().clearRect(0, 0, m_canvas.getWidth(), m_canvas.getHeight());

        for (Line line:lines) {
            m_overlay.getChildren().remove(line);
        }
        for (Circle circle:circles) {
            m_overlay.getChildren().remove(circle);

        }
        m_map = new Map();

        drawMap();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}