package view;

import controler.Controller;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javax.sound.midi.SysexMessage;
import java.util.List;

/**
 * Mouse gestures
 *
 * @author T-REXANOME
 */
public class MouseGestures {
    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
    List<Circle> circles;
    List<Line> lines;
    List<Arrow> arrows;
    Controller controller;
    Color currentcolor;

    protected double newTranslateX;
    protected double newTranslateY;

    /**
     * Constructor
     *
     * @param c controller
     */
    MouseGestures(Controller c) {
        controller = c;
    }

    /**
     * @param node
     */
    public void makeClickable(Node node) {
        node.setOnMouseEntered(circleOnMouseEnteredEventHandler);
        node.setOnMouseExited(circleOnMouseExitedEventHandler);
        node.setOnMouseClicked(circleOnMouseClickedEventHandler);
    }

    /**
     * @param node
     * @param circles
     * @param lines
     * @param arrows
     */
    public void makeMovable(Node node, List<Circle> circles, List<Line> lines, List<Arrow> arrows) {
        this.lines = lines;
        this.circles = circles;
        this.arrows = arrows;
        node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        node.setOnMousePressed(circleOnMousePressedEventHandler);
    }

    EventHandler<MouseEvent> circleOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
        /**
         *
         * @param t
         */
        @Override
        public void handle(MouseEvent t) {
            if (t.getSource() instanceof Circle) {
                Circle circle = ((Circle) (t.getSource()));
                circle.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));
                circle.setStrokeWidth(circle.getStrokeWidth() * 2);
                circle.setStroke(Color.RED);
                System.out.println(circle.getUserData());
                controller.leftClick((long) circle.getUserData());
            }
        }
    };

    EventHandler<MouseEvent> circleOnMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
        /**
         *
         * @param t
         */
        @Override
        public void handle(MouseEvent t) {

            if (t.getSource() instanceof Circle) {

                Circle p = ((Circle) (t.getSource()));
                currentcolor = (Color) p.getFill();
                p.setFill(Color.GREY.deriveColor(1, 1, 1, 0.7));
            }
        }
    };

    EventHandler<MouseEvent> circleOnMouseExitedEventHandler = new EventHandler<MouseEvent>() {
        /**
         *
         * @param t
         */
        @Override
        public void handle(MouseEvent t) {

            if (t.getSource() instanceof Circle) {

                Circle p = ((Circle) (t.getSource()));
                if (p.getRotate() != 1.0)
                    p.setFill(currentcolor);
            }
        }
    };

    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        /**
         *
         * @param t
         */
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
        /**
         *
         * @param t
         */
        @Override
        public void handle(MouseEvent t) {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            newTranslateX = orgTranslateX + offsetX;
            newTranslateY = orgTranslateY + offsetY;

            if (circles.get(1).getCenterX() < 100) {
                for (Circle circle : circles) {
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
            }
        }
    };
}
