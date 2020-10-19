package view;

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
import model.Segment;

import java.util.*;

/**
 * 
 */
public class GraphicalView implements Observer {

    int screenX = 1100;
    int screenY = 550;
    double zoomVal = 0.5;

    public static List<Circle> circles = new ArrayList<Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    public void drawShapes(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, screenX, screenY, 20, 20);
    }

    public void zoom() {
        zoomVal+= 1.0;
        for (int i = 0; i<lines.size();i++) {
            lines.remove(i);
        }
        for (int i = 0; i<circles.size();i++) {
            circles.remove(i);
        }
    }

    public void drawMap(Map map,Canvas canvas,Pane overlay) {

        canvas.setWidth(screenX);
        canvas.setHeight(screenY);
        overlay.setPrefWidth(screenX);
        overlay.setPrefHeight(screenY);

        double coeffX = (double)screenX/(554.64-554.57)*zoomVal;
        double coeffY = (double)screenY/(132.76-132.71)*zoomVal;
        double ordonneeX = 554.57*coeffX;
        double ordonneeY = 132.71*coeffY;

        for (HashMap.Entry mapentry : map.getListIntersections().entrySet()) {
            Intersection intersection = (Intersection) mapentry.getValue();
            double originX = (intersection.getLongitude() + 180) * (screenX / 360)*coeffX - ordonneeX;
            double originY = ((-1 * intersection.getLatitude()) + 90) * (screenY / 180)*coeffY - ordonneeY;
            double pointSize = 5.0;
            Circle circle = new Circle(pointSize*zoomVal);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
            circle.relocate(originX-pointSize*zoomVal, originY-pointSize*zoomVal);
            circles.add(circle);

            for ( Segment segment : intersection.getListSegments()) {
                Intersection destination = map.getListIntersections().get(segment.getDestination());
                double destinationX = (destination.getLongitude() + 180) * (screenX / 360)*coeffX - ordonneeX;
                double destinationY = ((-1 * destination.getLatitude()) + 90) * (screenY / 180)*coeffY - ordonneeY;
                Line line = new Line(originX, originY, destinationX, destinationY);
                lines.add(line);
            }

        }
        MouseGestures mg = new MouseGestures();
        mg.makeMovable(overlay, circles, lines);

        for (Line line:lines) {
            line.setStrokeWidth(4*zoomVal);
            overlay.getChildren().add(line);
        }

        for (Circle circle:circles) {
            mg.makeClickable(circle);
            overlay.getChildren().add(circle);
        }



    }

    @Override
    public void update(Observable o, Object arg) {

    }
}