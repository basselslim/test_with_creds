package controler;

import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmTest {

    Map map;

    @BeforeAll
    static void AlgorithmTest () {
        System.out.println("Test Of Algorithm Class");
    }

    @BeforeEach
    void dataInitialisation() {
        map = new Map();
        System.out.println("Data Initialisation");

        Depot depot = new Depot(2l, "");
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

        PickUpPoint p1 = new PickUpPoint(i3, 0);
        DeliveryPoint d1 = new DeliveryPoint(i7, 0);

        Request r1 = new Request(p1, d1);

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
        map.getListRequests().add(r1);
    }

    @Test
    void testComputeSmallestPaths() {
        // Arrange
        Algorithm algorithm = new Algorithm(map);
        List<Path> listPaths = new ArrayList<>();
        int answers[] = {22, 52, 30, 70, 52, 74};

        // Act
        HashMap<Long, HashMap<Long, Path>> CSPMap = algorithm.computeSmallestPaths();
        for(HashMap<Long, Path> h : CSPMap.values()) {
            listPaths.addAll(h.values());
        }
        //Assert
        assert(listPaths.size() == 6);
        for (int i=0; i<listPaths.size(); i++){
            assert(listPaths.get(i).getPathLength() == answers[i]);
        }
    }
}