package view;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.List;

public class MouseGestures {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    List<Circle> circles;
    List<Line> lines;

    public void makeClickable(Node node) {
        node.setOnMouseEntered(circleOnMouseEnteredEventHandler);
        node.setOnMouseExited(circleOnMouseExitedEventHandler);
        node.setOnMouseClicked(circleOnMouseClickedEventHandler);
    }

    public void makeMovable(Node node, List<Circle> circles, List<Line> lines) {
        this.lines = lines;
        this.circles = circles;
        node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        node.setOnMousePressed(circleOnMousePressedEventHandler);
    }

    EventHandler<MouseEvent> circleOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            if (t.getSource() instanceof Circle) {
                Circle circle = ((Circle) (t.getSource()));
                circle.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));

                if (circle.getRotate() != 1.0) {
                    circle.setRadius(circle.getRadius() * 2);
                    circle.setRotate(1.0);
                }
            }
        }
    };

    EventHandler<MouseEvent> circleOnMouseEnteredEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {


            if (t.getSource() instanceof Circle) {

                Circle p = ((Circle) (t.getSource()));

                p.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));

            }
        }
    };

    EventHandler<MouseEvent> circleOnMouseExitedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {


            if (t.getSource() instanceof Circle) {

                Circle p = ((Circle) (t.getSource()));
                if (p.getRotate() != 1.0)
                    p.setFill(Color.BLACK.deriveColor(1, 1, 1, 0.9));

            }
        }
    };

    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            Node p = ((Node) (t.getSource()));

            orgTranslateX = circles.get(1).getTranslateX();
            orgTranslateY = circles.get(1).getTranslateY();
        }
    };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            double newTranslateX = orgTranslateX + offsetX;
            double newTranslateY = orgTranslateY + offsetY;

            if (circles.get(1).getCenterX() < 100) {
                for (Circle circle : circles) {
                    circle.setTranslateX(newTranslateX);
                    circle.setTranslateY(newTranslateY);
                }

                for (Line line : lines) {
                    line.setTranslateX(newTranslateX);
                    line.setTranslateY(newTranslateY);

                }
            }
        }
    };

}
