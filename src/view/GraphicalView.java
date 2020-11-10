package view;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
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
    double RequestStrokeSize;
    double StepSiding;
    Boolean isMapClickable = false;
    Boolean setScreenSize = false;

    public GraphicalView(Map map, Pane overlay, MouseGestures mg) {
        m_map = map;
        mouseGestures = mg;
        mouseGestures.setGview(this);
        mg.newTranslateX = 0;
        mg.newTranslateY = 0;
        m_overlay = overlay;
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        screenX = (int)((screenBounds.getMaxX()-150)*0.595);
        screenY = (int)((screenBounds.getMaxY()-150)*0.762);
    }

    public static HashMap<Long,List<Circle>> circles = new HashMap<Long,List<Circle>>();

    public static List<Line> lines = new ArrayList<Line>();

    public static List<Arrow> arrows = new ArrayList<Arrow>();

    public static HashMap<Long,List<Rectangle>> rectangles = new HashMap<Long,List<Rectangle>>();

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

            drawPoint(intersection, Color.DIMGRAY, pointSize); //draw standard point
            drawMultipleLines(intersection, intersection.getListSegments());
        }

        //draw request points
        for (Request request : m_map.getListRequests()) {
            Step pickupStep = request.getPickUpPoint();
            Step deliveryStep = request.getDeliveryPoint();
            Color color = generateColor(pickupStep.getRequest());
            drawRectangle(pickupStep, color, ReqpointSize);
            drawRequestPoint(deliveryStep, color, ReqpointSize);


        }

        //draw best tour
        if (!m_map.getDeliveryTour().getListPaths().isEmpty() && m_map.getDeliveryTour() != null) {
            System.out.println("ENTREE");
            for (Path path : m_map.getDeliveryTour().getListPaths()) {
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

    public void resetView() {
        zoomVal = 1.0;
        long id = (long)circles.keySet().toArray()[0];
        zoomTranslateX = 0;
        zoomTranslateY = 0;
        mouseGestures.newTranslateX = 0;
        mouseGestures.newTranslateY = 0;
        refreshMap();
    }

    public void drawMouseSelection(Step step) {
        List<Circle> CircleList = circles.get(step.getId());
        for (Circle circle : CircleList) {
            if(circle.getUserData() instanceof Step) {
                Step MapStep = (Step) circle.getUserData();
                if (MapStep.getRequest() == step.getRequest()) {
                    circle.setStrokeWidth(circle.getStrokeWidth() * 1.5);
                    circle.setStroke(Color.RED);
                }
            }
        }

        List<Rectangle> RectangleList = rectangles.get(step.getId());
        if (RectangleList != null) {
            for (Rectangle rectangle : RectangleList) {
                if (rectangle.getUserData() instanceof Step) {
                    Step MapStep = (Step) rectangle.getUserData();
                    if (MapStep.getRequest() == step.getRequest()) {
                        rectangle.setStrokeWidth(rectangle.getStrokeWidth() * 1.5);
                        rectangle.setStroke(Color.RED);
                    }
                }
            }
        }
    }

    public void undrawMouseSelection(Step step) {
        List<Circle> CircleList = circles.get(step.getId());
        for (Circle circle : CircleList) {
            if(circle.getUserData() instanceof Step) {
                Step MapStep = (Step) circle.getUserData();
                if (MapStep.getRequest() == step.getRequest()) {
                    circle.setStrokeWidth(circle.getStrokeWidth() / 1.5);
                    circle.setStroke(Color.BLACK);
                }
            }
        }

        List<Rectangle> RectangleList = rectangles.get(step.getId());
        if (RectangleList != null) {
            for (Rectangle rectangle : RectangleList) {
                if (rectangle.getUserData() instanceof Step) {
                    Step MapStep = (Step) rectangle.getUserData();
                    if (MapStep.getRequest() == step.getRequest()) {
                        rectangle.setStrokeWidth(rectangle.getStrokeWidth() / 1.5);
                        rectangle.setStroke(Color.BLACK);
                    }
                }
            }
        }
    }

    public void drawMouseSelection(long NodeId) {
        Circle circle = circles.get(NodeId).get(0);
        if (circle != null) {
            //circle.setFill(Color.DARKGREY.deriveColor(1, 1, 1, 0.9));
            circle.setStrokeWidth(circle.getStrokeWidth() * 2.0);
            circle.setStroke(Color.RED);
        }

    }
        public void undrawMouseSelection (long NodeId){
            Circle circle = circles.get(NodeId).get(0);

            if (circle != null) {
                //circle.setFill(Color.BLACK);
                circle.setStrokeWidth(circle.getStrokeWidth() / 2.0);
                circle.setStroke(Color.DIMGRAY);
            }

        }

        private void drawMultipleLines (Intersection origin, List < Segment > segmentList){

            double originX = longToPix(origin.getLongitude());
            double originY = latToPix(origin.getLatitude());

            for (Segment segment : segmentList) {
                Intersection destination = m_map.getListIntersections().get(segment.getDestination());
                double destinationX = longToPix(destination.getLongitude());
                double destinationY = latToPix(destination.getLatitude());
                double length = Math.sqrt(Math.pow(destinationX - originX, 2) + Math.pow(destinationY - originY, 2));
                Line line = new Line(originX, originY, destinationX, destinationY);
                line.setStroke(Color.GREY);
                line.setStrokeWidth(StrokeSize);
                lines.add(line);
            }
        }

        private void drawArrow (Intersection origin, Intersection destination,double size){

            double originX = longToPix(origin.getLongitude());
            double originY = latToPix(origin.getLatitude());
            double destinationX = longToPix(destination.getLongitude());
            double destinationY = latToPix(destination.getLatitude());
            Arrow arrow = new Arrow(originX, originY, destinationX, destinationY, 6.0);
            arrow.setFill(Color.BLUE);
            arrow.setStrokeWidth(StrokeSize * 1.5);
            arrows.add(arrow);
        }

        private void drawPoint (Intersection intersection, Color color,double size){

            double pointX = longToPix(intersection.getLongitude());
            double pickupY = latToPix(intersection.getLatitude());

            Circle circle = new Circle(size);
            circle.setStroke(Color.DIMGRAY);
            circle.setStrokeWidth(StrokeSize);
            circle.setFill(color.deriveColor(1, 1, 1, 1.0));
            circle.relocate(pointX - size, pickupY - size);
            circle.setUserData(intersection.getId());

            List<Circle> CircleList = new ArrayList<Circle>();
            CircleList.add(circle);
            circles.put(intersection.getId(), CircleList);

        }

        private void drawRequestPoint (Step step, Color color,double size){

            double pointX = longToPix(step.getLongitude());
            double pointY = latToPix(step.getLatitude());
            long id = step.getId();

            Circle circle = new Circle(size);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(RequestStrokeSize);
            circle.setFill(color.deriveColor(1, 1, 1, 1.0));
            circle.relocate(pointX - size, pointY - size);
            circle.setUserData((Step)(step.getRequest().getDeliveryPoint()));
            circle.setViewOrder(-1.0);
            int order = circles.get(id).size()-1;
            if(m_map.getDepot().getId() == id)
                order++;
            if(rectangles.get(id)!=null)
                    order += rectangles.get(id).size();
            System.out.println(order);
            circle.relocate(pointX - size + order*StepSiding, pointY - size + order*StepSiding);
            circles.get(id).add(circle);
        }

        private void drawRectangle (Step step, Color color,double size){
            double pointX = longToPix(step.getLongitude());
            double pointY = latToPix(step.getLatitude());
            long id = step.getId();

            Rectangle rectangle = new Rectangle(pointX - size, pointY - size, size * 2, size * 2);
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeWidth(RequestStrokeSize);
            rectangle.setFill(color.deriveColor(1, 1, 1, 1.0));

            rectangle.setUserData(step.getRequest().getPickUpPoint());
            //circles.get(id).remove(0);
            if (rectangles.containsKey(id)) {
                int order = circles.get(id).size() + rectangles.get(id).size()-1;
                if(m_map.getDepot().getId() == id)
                    order++;
                rectangle.relocate(pointX - size + order*StepSiding, pointY - size + order*StepSiding);
                rectangles.get(id).add(rectangle);
            } else {
                List<Rectangle> RectangleList = new ArrayList<Rectangle>();
                int order = circles.get(id).size()-1;
                rectangle.relocate(pointX - size + order*StepSiding, pointY - size + order*StepSiding);
                RectangleList.add(rectangle);
                rectangles.put(id, RectangleList);
            }
        }

        private void addNodesToOverlay () {
            for (Line line : lines) {
                m_overlay.getChildren().add(line);
            }

            Circle depot = new Circle();
            for (HashMap.Entry<Long, List<Circle>> mapentry : circles.entrySet()) {
                List<Circle> CircleList = mapentry.getValue();
                for (Circle circle : CircleList) {
                    if ((long) mapentry.getKey() == m_map.getDepot().getId()) {
                        depot = circle;
                        depot.setRadius(pointSize * 2);
                        depot.setFill(Color.RED);
                        depot.setStroke(Color.BLACK);
                        depot.setStrokeWidth(StrokeSize * 1.5);
                        depot.setViewOrder(-1.0);
                        depot.setUserData(m_map.getDepot().getId());
                    } else
                        m_overlay.getChildren().add(circle);
                }
            }
            List<Circle> DepotCircleList = new ArrayList<>();
            circles.put(m_map.getDepot().getId(),DepotCircleList);
            circles.get(m_map.getDepot().getId()).add(depot);
            m_overlay.getChildren().add(depot);

            for (Arrow arrow : arrows) {
                m_overlay.getChildren().add(arrow);
            }

            for (HashMap.Entry<Long, List<Rectangle>> mapentry : rectangles.entrySet()) {
                List<Rectangle> RectangleList = mapentry.getValue();
                for (Rectangle rectangle : RectangleList) {
                    m_overlay.getChildren().add(rectangle);
                }
            }
        }

        private void updateCoeff () {
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
            RequestStrokeSize = 0.00028 * coeffX/zoomVal;
            StepSiding = 0.00040 * coeffX;
        }

        private void updateTranslation ( double XValue, double YValue){

            for (HashMap.Entry<Long, List<Circle>> mapentry : circles.entrySet()) {
                List<Circle> CircleList = mapentry.getValue();
                for (Circle circle : CircleList) {
                    circle.setTranslateX(XValue);
                    circle.setTranslateY(YValue);
                }
            }

            for (Line line : lines) {
                line.setTranslateX(XValue);
                line.setTranslateY(YValue);
            }
            for (Arrow arrow : arrows) {
                arrow.setTranslateX(XValue);
                arrow.setTranslateY(YValue);
            }

            for (HashMap.Entry<Long, List<Rectangle>> mapentry : rectangles.entrySet()) {
                List<Rectangle> RectangleList = mapentry.getValue();
                for (Rectangle rectangle : RectangleList) {
                    rectangle.setTranslateX(XValue);
                    rectangle.setTranslateY(YValue);
                }

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

    private Color generateColor(Request req){
        Random generator = new Random(req.getDeliveryPoint().getId() + req.getPickUpPoint().getId()+1);
        int rand = generator.nextInt(150);
        int rand2 = generator.nextInt(150);
        int rand3 = generator.nextInt(150);
        Color color = Color.rgb(rand + 100,(rand2 + 100),(rand3 + 100));
        return  color;
    }

    @Override
    public void update(observer.Observable observed, Object arg) {
        refreshMap();
    }


}