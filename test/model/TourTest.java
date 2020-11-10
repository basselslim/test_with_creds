package model;

import controler.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Tour t = new Tour();
        //Assert
        assertNotNull(t);
    }

    @Test
    void constructorMapWorking(){
        //Arrange
        Map m = new Map();
        //Act
        Tour t = new Tour(m);
        //Assert
        assertEquals(m,t.getMap());
        assertNotNull(t.getListPaths());
        assertNotNull(t.getListTimes());
        assertNotNull(t.getListRequestsIntersection());
    }
    @Test
    void constructorMapListWorking(){
        //Arrange
        Map m = new Map();
        Depot d = new Depot(1,"12:00");
        m.setDepot(d);
        Step s1 = new Step(1,2,3);
        Step s2 = new Step(1,2,4);
        List<Segment> listSegments = new LinkedList<Segment>();
        Path p = new Path(listSegments,s1,s2);
        List<Path> listPaths = new LinkedList<Path>();
        listPaths.add(p);
        //Act
        Tour t = new Tour(m,listPaths);
        //Assert
        assertEquals(m,t.getMap());
        assertEquals(listPaths,t.getListPaths());
        assertNotNull(t.getListTimes());
        assertNotNull(t.getListRequestsIntersection());
    }
    @Test
    void constructorMapListWorkingWhenListNull(){
        //Arrange
        Map m = new Map();
        Depot d = new Depot(1,"12:00");
        m.setDepot(d);
        List<Segment> listSegments = new LinkedList<Segment>();
        List<Path> listPaths = new LinkedList<Path>();
        //Act
        Tour t = new Tour(m,listPaths);
        //Assert
        assertEquals(m,t.getMap());
        assertEquals(listPaths,t.getListPaths());
        assertNotNull(t.getListTimes());
        assertNotNull(t.getListRequestsIntersection());
    }
    @Test
    void getTourLength(){
        //Arrange
        Map m = new Map();
        Depot d = new Depot(1,"12:00");
        m.setDepot(d);
        Step s1 = new Step(1,2,3);
        Step s2 = new Step(1,2,4);
        Segment s = new Segment(12,"Rue",3);
        List<Segment> listSegments = new LinkedList<Segment>();
        listSegments.add(s);
        Path p = new Path(listSegments,s1,s2);
        List<Path> listPaths = new LinkedList<Path>();
        listPaths.add(p);
        Tour t = new Tour(m,listPaths);
        //Act
        int a =t.getTourLength();
        //Assert
        assertEquals(12,a);
    }
    @Test
    void getListTimes(){
        //Arrange
        Map m = new Map();
        Tour t = new Tour();
        List<int[]> l = new LinkedList<int[]>();
        t.setListTimes(l);
        //Act
        List<int[]> lt = new LinkedList<int[]>();
        //Assert
        assertEquals(l,lt);
    }
    @Test
    void groupRequestIntersection(){
        //Arrange
        Map m = new Map();

        Depot d = new Depot(1,"12:00");
        m.setDepot(d);

        ArrayList<Request> lisRequest = new ArrayList<Request>();
        PickUpPoint p1 = new PickUpPoint(2,3,3,3);
        DeliveryPoint d1 = new DeliveryPoint(3,3,3,3);
        Request r = new Request(p1,d1);
        lisRequest.add(r);
        m.setListRequest(lisRequest);

        Intersection i1 = new Intersection(1,2,3);
        Intersection i2 = new Intersection(2,2,3);
        Intersection i3 = new Intersection(3,2,3);
        HashMap<Long,Intersection> list = new HashMap<Long,Intersection>();
        ArrayList<Intersection> list1 = new ArrayList<Intersection>();
        ArrayList<Intersection> list2 = new ArrayList<Intersection>();
        ArrayList<Intersection> list3 = new ArrayList<Intersection>();
        list.put(1l,i1);
        list.put(2l,i2);
        list.put(3l,i3);
        list1.add(i1);
        list2.add(p1);
        list3.add(d1);
        m.setListIntersections(list);


        Tour t = new Tour(m);
        //Act
        t.groupRequestIntersections();
        HashMap<Long,ArrayList<Intersection>> listRequestsIntersection = t.getListRequestsIntersection();
        //Assert
        assertEquals(list1,listRequestsIntersection.get(1l));
        assertEquals(list2,listRequestsIntersection.get(2l));
        assertEquals(list3,listRequestsIntersection.get(3l));
    }
    @Test
    void writeTextForInterestPoint(){
        //Arrange
        Map m = new Map();

        Depot d = new Depot(1,"12:00");
        m.setDepot(d);

        ArrayList<Request> lisRequest = new ArrayList<Request>();
        PickUpPoint p1 = new PickUpPoint(2,3,3,3);
        DeliveryPoint d1 = new DeliveryPoint(3,3,3,3);
        Request r = new Request(p1,d1);
        lisRequest.add(r);
        m.setListRequest(lisRequest);

        Intersection i1 = new Intersection(1,2,3);
        Intersection i2 = new Intersection(2,2,3);
        Intersection i3 = new Intersection(3,2,3);
        HashMap<Long,Intersection> list = new HashMap<Long,Intersection>();
        ArrayList<Intersection> list1 = new ArrayList<Intersection>();
        ArrayList<Intersection> list2 = new ArrayList<Intersection>();
        ArrayList<Intersection> list3 = new ArrayList<Intersection>();
        list.put(1l,i1);
        list.put(2l,i2);
        list.put(3l,i3);
        list1.add(i1);
        list2.add(p1);
        list3.add(d1);
        m.setListIntersections(list);

        Tour t = new Tour(m);
        t.groupRequestIntersections();


        //Act
        String str1 = t.writeTextForInterestPoint(2);
        String str2 = t.writeTextForInterestPoint(3);

        //Assert
        assertEquals("   - You have to pick up 1 package(s) at this intersection. This may take 3 seconds\n\n",str1);
        assertEquals("   - You have to deliver 1 package(s) at this intersection. This may take 3 seconds\n\n",str2);
    }
    @Test
    void generateTextForRoadMap(){
        //Arrange
        Map m = new Map();

        Depot d = new Depot(1,"12:00");
        m.setDepot(d);

        ArrayList<Request> lisRequest = new ArrayList<Request>();
        PickUpPoint p1 = new PickUpPoint(2,3,3,3);
        DeliveryPoint d1 = new DeliveryPoint(3,3,3,3);
        Request r = new Request(p1,d1);
        lisRequest.add(r);
        m.setListRequest(lisRequest);

        Intersection i1 = new Intersection(1,2,3);
        Intersection i2 = new Intersection(2,2,3);
        Intersection i3 = new Intersection(3,2,3);
        HashMap<Long,Intersection> list = new HashMap<Long,Intersection>();
        ArrayList<Intersection> list1 = new ArrayList<Intersection>();
        ArrayList<Intersection> list2 = new ArrayList<Intersection>();
        ArrayList<Intersection> list3 = new ArrayList<Intersection>();
        list.put(1l,i1);
        list.put(2l,i2);
        list.put(3l,i3);
        list1.add(i1);
        list2.add(p1);
        list3.add(d1);
        m.setListIntersections(list);

        Segment s = new Segment(12,"Rue",3);
        List<Segment> listSegments = new LinkedList<Segment>();
        listSegments.add(s);
        Path p = new Path(listSegments,p1,d1);
        List<Path> listPaths = new LinkedList<Path>();
        listPaths.add(p);

        Tour t = new Tour(m);
        t.setListPaths(listPaths);
        t.groupRequestIntersections();
        t.populateListTimes();
        String stringReference = "Roadmap \n" +
                "\n" +
                "   - Departure from depot at 12:00\n" +
                "\n" +
                "Step n°1:  Arrive at 12:00\n" +
                "\n" +
                "   - You have to deliver 2 package(s) at this intersection. This may take 6 seconds\n" +
                "\n" +
                "   - You have to deliver 2 package(s) at this intersection. This may take 6 seconds\n\n";


        //Act
        String str1 = t.generateTextForRoadMap();


        //Assert
        assertEquals(stringReference,str1);
    }
    @Test
    void checkTimeUnderOneDayWorking() {
        //Arrange
        Tour t = new Tour();
        int timeOk = 12600;
        int timeNotOk = 132000;
        int referenceNotOk = 45600;


        //Act
        int res1 = t.checkTimeUnderOneDay(timeOk);
        int res2 = t.checkTimeUnderOneDay(timeNotOk);


        //Assert
        assertEquals(timeOk, res1);
        assertEquals(referenceNotOk, res2);
    }
    @Test
    void timeToStringWorking() {
        //Arrange
        Tour t = new Tour();
        int time1 = 14400;
        int time2 = 14460;
        int time3 = 16200;
        String reference1 = "4:00";
        String reference2 = "4:01";
        String reference3 = "4:30";


        //Act
        String res1 = t.timeToString(time1);
        String res2 = t.timeToString(time2);
        String res3 = t.timeToString(time3);


        //Assert
        assertEquals(reference1, res1);
        assertEquals(reference2, res2);
        assertEquals(reference3, res3);
    }
    @Test
    void stringToTimeWorking() {
        //Arrange
        Tour t = new Tour();
        String str1 = "8:0:0";
        String str2 = "12:30";
        int reference1 = 28800;
        int reference2 = 45000;


        //Act
        int res1 = t.stringToTime(str1);
        int res2 = t.stringToTime(str2);


        //Assert
        assertEquals(reference1, res1);
        assertEquals(reference2, res2);
    }
    @Test
    void populateListTimesWorking() {
        //Arrange
        Map m = new Map();

        Depot d = new Depot(1, "12:00");
        m.setDepot(d);

        ArrayList<Request> lisRequest = new ArrayList<Request>();
        PickUpPoint p1 = new PickUpPoint(2, 3, 3, 60);
        DeliveryPoint d1 = new DeliveryPoint(3, 3, 3, 120);
        Request r = new Request(p1, d1);
        lisRequest.add(r);
        m.setListRequest(lisRequest);

        Intersection i1 = new Intersection(1, 2, 3);
        Intersection i2 = new Intersection(2, 2, 3);
        Intersection i3 = new Intersection(3, 2, 3);
        HashMap<Long, Intersection> list = new HashMap<Long, Intersection>();
        ArrayList<Intersection> list1 = new ArrayList<Intersection>();
        ArrayList<Intersection> list2 = new ArrayList<Intersection>();
        ArrayList<Intersection> list3 = new ArrayList<Intersection>();
        list.put(1l, i1);
        list.put(2l, i2);
        list.put(3l, i3);
        list1.add(i1);
        list2.add(p1);
        list3.add(d1);
        m.setListIntersections(list);

        Segment s1 = new Segment(167, "Rue1", 2);
        List<Segment> listSegments1 = new LinkedList<Segment>();
        listSegments1.add(s1);
        Segment s2 = new Segment(167, "Rue2", 3);
        List<Segment> listSegments2 = new LinkedList<Segment>();
        listSegments2.add(s2);
        Segment s3 = new Segment(167, "Rue3", 1);
        List<Segment> listSegments3 = new LinkedList<Segment>();
        listSegments3.add(s3);
        Path pa1 = new Path(listSegments1, d, p1);
        Path pa2 = new Path(listSegments2, p1, d1);
        Path pa3 = new Path(listSegments3, d1, d);
        List<Path> listPaths = new LinkedList<Path>();
        listPaths.add(pa1);
        listPaths.add(pa2);
        listPaths.add(pa3);

        Tour t = new Tour(m);
        t.setListPaths(listPaths);
        t.groupRequestIntersections();

        int reference00 = 43200;
        int reference01 = 43260;
        int reference10 = 43320;
        int reference11 = 43380;
        int reference20 = 43500;
        int reference21 = 43560;


        //Act
        t.populateListTimes();
        List<int[]> listTimes = t.getListTimes();


        //Assert
        assertEquals(reference00, listTimes.get(0)[0]);
        assertEquals(reference01, listTimes.get(0)[1]);
        assertEquals(reference10, listTimes.get(1)[0]);
        assertEquals(reference11, listTimes.get(1)[1]);
        assertEquals(reference20, listTimes.get(2)[0]);
        assertEquals(reference21, listTimes.get(2)[1]);
    }

}