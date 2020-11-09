package controler;

import com.sun.jdi.connect.spi.TransportService;
import model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.UniqueId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravellingSalesmanProblemTest {

    //dataset
    Map map;
    HashMap<Long, HashMap<Long,Path>> adjacencyMatrixOfShortestPath;
    TravellingSalesmanProblem TSP;

    @BeforeAll
    static void travellingSalesmanProblemTest () {
        System.out.println("Test Of TravellingSalesManProblem Class");
    }

    @BeforeEach
    void dataInitialisation() {
        System.out.println("Data Initialisation");

        //Arrange
        map = new Map();
        adjacencyMatrixOfShortestPath = new HashMap<Long, HashMap<Long,Path>>();


        Depot depot = new Depot(1l,"8:30:200");
        Intersection a = new Intersection(1l,0,0);
        Intersection b = new Intersection(4l,0,0);
        Intersection c = new Intersection(7l,0,0);
        Intersection d = new Intersection(9l,0,0);

        //don't touch a intersection in order to keep the test succes
        a.getListSegments().add(new Segment(10.0,"",4l));
        a.getListSegments().add(new Segment(25.0,"",7l));
        a.getListSegments().add(new Segment(35.0,"",9l));

        b.getListSegments().add(new Segment(55.0,"",1l));
        b.getListSegments().add(new Segment(5.0,"",7l));
        b.getListSegments().add(new Segment(19.0,"",9l));

        c.getListSegments().add(new Segment(500.0,"",1l));
        c.getListSegments().add(new Segment(250.0,"",4l));
        c.getListSegments().add(new Segment(10.0,"",9l));

        d.getListSegments().add(new Segment(205,"",1l));
        d.getListSegments().add(new Segment(200.0,"",4l));
        d.getListSegments().add(new Segment(150.0,"",7l));

        map.getListIntersections().put(1l,a);
        map.getListIntersections().put(4l,b);
        map.getListIntersections().put(7l,c);
        map.getListIntersections().put(9l,d);
        map.setDepot(depot);

        for(java.util.Map.Entry<Long,Intersection> mapentry : map.getListIntersections().entrySet()){
            HashMap<Long,Path> adjacencyList = new HashMap<Long,Path>();
            for (Segment s : mapentry.getValue().getListSegments()) {
                List<Segment> listSegments = new LinkedList<Segment>();
                listSegments.add(s);
                adjacencyList.put(s.getDestination(),new Path(listSegments,mapentry.getKey(),s.getDestination()));
            }
            adjacencyMatrixOfShortestPath.put(mapentry.getKey(),adjacencyList);
        }
        TSP = new TravellingSalesmanProblem(map,adjacencyMatrixOfShortestPath);
    }

    @Test
    void testCopyToFinal() {
        System.out.println("CopyToFinalTest");
        //Arrange
        Path aToB = adjacencyMatrixOfShortestPath.get(1l).get(4l);
        Path bToC = adjacencyMatrixOfShortestPath.get(4l).get(7l);
        Path cToD = adjacencyMatrixOfShortestPath.get(7l).get(9l);
        int finalpathLengthExpected = aToB.getPathLength() + bToC.getPathLength() + cToD.getPathLength()
                + adjacencyMatrixOfShortestPath.get(9l).get(1l).getPathLength();
        List<Path> tour = new LinkedList<Path>();
        tour.add(aToB);
        tour.add(bToC);
        tour.add(cToD);
        Tour randomTour = new Tour(map,tour);

        //Act
        TSP.copyToFinal(randomTour);

        //Assert
        assert(TSP.final_tour.getTourLength() == finalpathLengthExpected);

    }

    @Test
    void testFirstMin() {
        System.out.println("FirstMinTest");
        //Act
        int min  = TSP.firstMin(1l);
        //Assert
        assert (min == 10);
    }

    @Test
    void testSecondMin() {
        System.out.println("SecondMinTest");
        //Act
        int secondMin  = TSP.secondMin(1l);
        //Assert
        assert (secondMin == 25);
    }

    @Test
    void testTSP() {
        System.out.println("TSPTest");
        //Arrange
        int a, b;
        //Act
        //TSP.TSP(50);
        //int a = TSP.final_res;
        //TSP.TSP(200);
        //int b = TSP.final_res;
    }
}