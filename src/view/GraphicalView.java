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

import java.util.*;

/**
 * 
 */
public class GraphicalView implements Observer {

    public static List<Circle> circles = new ArrayList<Circle>();

    public static List<Line> lines = new ArrayList<Line>();

    public void drawLines(Canvas canvas, Pane overlay){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Circle circle1 = new Circle(10);
        circle1.setStroke(Color.BLACK);
        circle1.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
        circle1.relocate(100, 100);

        Circle circle2 = new Circle(10);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
        circle2.relocate(200, 200);

        Circle circle3 = new Circle(10);
        circle3.setStroke(Color.BLACK);
        circle3.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
        circle3.relocate(350, 250);

        Circle circle4 = new Circle(10);
        circle4.setStroke(Color.BLACK);
        circle4.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
        circle4.relocate(100, 450);

        Circle circle5 = new Circle(10);
        circle5.setStroke(Color.BLACK);
        circle5.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));
        circle5.relocate(450, 150);

        Line line1 = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle2.getLayoutX(), circle2.getLayoutY());
        Line line2 = new Line(circle2.getLayoutX(), circle2.getLayoutY(), circle3.getLayoutX(), circle3.getLayoutY());
        Line line3 = new Line(circle3.getLayoutX(), circle3.getLayoutY(), circle4.getLayoutX(), circle4.getLayoutY());
        Line line4 = new Line(circle2.getLayoutX(), circle2.getLayoutY(), circle4.getLayoutX(), circle4.getLayoutY());
        Line line5 = new Line(circle3.getLayoutX(), circle3.getLayoutY(), circle5.getLayoutX(), circle5.getLayoutY());
        Line line6 = new Line(circle1.getLayoutX(), circle1.getLayoutY(), circle5.getLayoutX(), circle5.getLayoutY());

        lines.add(line1);
        lines.add(line2);
        lines.add(line3);
        lines.add(line4);
        lines.add(line5);
        lines.add(line6);

        circles.add(circle1);
        circles.add(circle2);
        circles.add(circle3);
        circles.add(circle4);
        circles.add(circle5);



        MouseGestures mg = new MouseGestures();
        mg.makeMovable(overlay, circles, lines);

        for (Line line:lines) {
            line.setStrokeWidth(5);
            overlay.getChildren().add(line);
        }

        for (Circle circle:circles) {
            mg.makeClickable(circle);
            overlay.getChildren().add(circle);
        }



    }

    public void drawShapes(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(0, 0, 1200, 600, 20, 20);
    }

    public void drawMap(Map map,Canvas canvas,Pane overlay) {

        
        System.out.println("Dessin de la map");
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}