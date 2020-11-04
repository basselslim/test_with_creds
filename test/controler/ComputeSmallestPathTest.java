package controler;

import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.IntersectionType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComputeSmallestPathTest {

    Map map;

    @BeforeAll
    static void travellingSalesmanProblemTest () {
        System.out.println("Test Of ComputeSmallestPath Class");
    }

    @BeforeEach
    void dataInitialisation() {
        map = new Map();
        System.out.println("Data Initialisation");

        Depot depot = new Depot(1l, "");
        Intersection i1 = new Intersection(1l, 45.4, 4.1);
        Intersection i2 = new Intersection(2l, 45.4, 4.2);
        Intersection i3 = new Intersection(3l, 45.6, 4.3);
        Intersection i4 = new Intersection(4l, 45.4, 4.3);
        Intersection i5 = new Intersection(5l, 45.1, 4.3);
        Intersection i6 = new Intersection(6l, 45.4, 4.4);
        Intersection i7 = new Intersection(7l, 45.1, 4.5);
        Intersection i8 = new Intersection(8l, 45.5, 4.6);
        Intersection i9 = new Intersection(9l, 45.4, 4.6);

        i1.getListSegments().add(new Segment(10.0, "", 2l));

        i2.getListSegments().add(new Segment(10.0, "", 1l));
        i2.getListSegments().add(new Segment(22.0, "", 3l));
        i2.getListSegments().add(new Segment(10.0, "", 4l));
        i2.getListSegments().add(new Segment(32.0, "", 5l));

        i3.getListSegments().add(new Segment(20.0, "", 4l));

        i4.getListSegments().add(new Segment(10.0, "", 2l));
        i4.getListSegments().add(new Segment(30.0, "", 5l));
        i4.getListSegments().add(new Segment(10.0, "", 6l));

        i5.getListSegments().add(new Segment(30.0, "", 4l));
        i5.getListSegments().add(new Segment(20.0, "", 7l));

        i6.getListSegments().add(new Segment(10.0, "", 4l));
        i6.getListSegments().add(new Segment(14.0, "", 8l));
        i6.getListSegments().add(new Segment(10.0, "", 9l));

        i7.getListSegments().add(new Segment(32.0, "", 6l));

        i8.getListSegments().add(new Segment(10.0, "", 9l));

        i9.getListSegments().add(new Segment(10.0, "", 6l));
        i9.getListSegments().add(new Segment(10.0, "", 8l));

        map.getListIntersections().put(1l, i1);
        map.getListIntersections().put(2l, i2);
        map.getListIntersections().put(3l, i3);
        map.getListIntersections().put(4l, i4);
        map.getListIntersections().put(5l, i5);
        map.getListIntersections().put(6l, i6);
        map.getListIntersections().put(7l, i7);
        map.getListIntersections().put(8l, i8);
        map.getListIntersections().put(9l, i9);
        map.setDepot(depot);
    }

    @Test
    void testComputeSmallestPath() {
        // Arrange
        Intersection from = map.getListIntersections().get(1l);
        Intersection to = map.getListIntersections().get(8l);
        ComputeSmallestPath CSP = new ComputeSmallestPath(map);
        List<Intersection> listIntersection = new ArrayList();

        // Act
        listIntersection = CSP.computeSmallestPath(from, to);

        // Assert
        assert(listIntersection.get(0).getId() == 2l &&
                listIntersection.get(1).getId() == 4l &&
                listIntersection.get(2).getId() == 6l &&
                listIntersection.get(3).getId() == 8l);
    }
}