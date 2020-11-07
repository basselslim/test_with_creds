package view;

import controler.Controller;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;

public class MouseGestures {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    HashMap<Long,Circle> circles;
    List<Line> lines;
    List<Arrow> arrows;
    HashMap<Long,Rectangle> rectangles;

    Controller controller;
    GraphicalView Gview;

    Color currentcolor;

    protected double newTranslateX;
    protected double newTranslateY;

    public MouseGestures(Controller c) {
        controller = c;
    }

    public void makeClickable(Node node) {
        node.setOnMouseEntered(nodeOnMouseEnteredEventHandler);
        node.setOnMouseExited(nodeOnMouseExitedEventHandler);
        node.setOnMouseClicked(nodeOnMouseClickedEventHandler);
    }

    public void makeMovable(Node node, HashMap<Long,Circle> circles, List<Line> lines, List<Arrow> arrows, HashMap<Long,Rectangle> rectangles) {
        this.lines = lines;
        this.circles = circles;
        this.arrows = arrows;
        this.rectangles = rectangles;
        node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        node.setOnMousePressed(circleOnMousePressedEventHandler);
    }

    EventHandler<MouseEvent> nodeOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getSource() instanceof Circle) {
                Circle circle = ((Circle) (t.getSource()));
                controller.leftClick((long)circle.getUserData());
            }

            else if(t.getSource() instanceof Rectangle) {
                Rectangle rectangle = ((Rectangle) (t.getSource()));
                controller.leftClick((long)rectangle.getUserData());
            }
        }
    };

    EventHandler<MouseEvent> nodeOnMouseEnteredEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {

            if (t.getSource() instanceof Circle) {

                Circle circle = ((Circle) (t.getSource()));
                currentcolor = (Color) circle.getFill();
                circle.setFill(Color.GREY.deriveColor(1, 1, 1, 0.9));
                controller.mouseOn((long) circle.getUserData());
            } else if (t.getSource() instanceof Rectangle) {

                Rectangle rectangle = ((Rectangle) (t.getSource()));
                currentcolor = (Color) rectangle.getFill();
                rectangle.setFill(Color.GREY.deriveColor(1, 1, 1, 0.9));
                controller.mouseOn((long) rectangle.getUserData());
            }
        }
    };

    EventHandler<MouseEvent> nodeOnMouseExitedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            if (t.getSource() instanceof Circle) {

                Circle circle = ((Circle) (t.getSource()));
                if (circle.getRotate() != 1.0)
                    circle.setFill(currentcolor);

            } else if (t.getSource() instanceof Rectangle) {

                Rectangle rectangle = ((Rectangle) (t.getSource()));
                if (rectangle.getRotate() != 1.0)
                    rectangle.setFill(currentcolor);

            }
        }
    };

    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            Node p = ((Node) (t.getSource()));
            HashMap.Entry entry = circles.entrySet().iterator().next();
            long Id = (long)entry.getKey();
            orgTranslateX = circles.get(Id).getTranslateX();
            orgTranslateY = circles.get(Id).getTranslateY();
        }
    };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            newTranslateX = orgTranslateX + offsetX;
            newTranslateY = orgTranslateY + offsetY;

            //Boolean XBlocking = newTranslateX+controller.getMap().findMinLat() > 1200 ;
            //Boolean YBlocking = newTranslateY+controller.getMap().findMinLong() > 800 ;

            //if(XBlocking && YBlocking){

            for (HashMap.Entry mapentry : circles.entrySet()) {
                Circle circle = (Circle) mapentry.getValue();
                circle.setTranslateX(newTranslateX);
                circle.setTranslateY(newTranslateY);
            }

            for (Line line : lines) {
                line.setTranslateX(newTranslateX);
                line.setTranslateY(newTranslateY);

            }
            for (Arrow arrow : arrows) {
                arrow.setTranslateX(newTranslateX);
                arrow.setTranslateY(newTranslateY);

            }
            for (HashMap.Entry mapentry : rectangles.entrySet()) {
                Rectangle rectangle = (Rectangle) mapentry.getValue();
                rectangle.setTranslateX(newTranslateX);
                rectangle.setTranslateY(newTranslateY);

            }
        }
        //}

    };



    public void setGview(GraphicalView graphicalView) {
        Gview = graphicalView;
    }
}
