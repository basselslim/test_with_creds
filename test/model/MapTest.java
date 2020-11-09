package model;

import org.junit.jupiter.api.Test;

import java.awt.image.RescaleOp;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void emptyConstructorAndInitializedElementsNotNull() {
        //Arrange

        //Act
        Map m = new Map();
        //Assert
        assertNotNull(m);
        assertNotNull(m.getDepot());
        assertNotNull(m.getListRequests());
        assertNotNull(m.listIntersections);
        assertNotNull(m.getDeliveryTour());

    }

    @Test
    void constructorWithoutListWorking() {
        //Arrange
        long id1 = 1;
        long id2 = 2;
        Intersection i1 = new Intersection();
        Intersection i2 = new Intersection();
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(id1, i1);
        listIntersection.put(id2, i2);
        //Act
        Map m = new Map(listIntersection);
        //Assert
        assertEquals(listIntersection,m.getListIntersections());
        assertNotNull(m.getDepot());
        assertNotNull(m.getListRequests());
        assertNotNull(m.getDeliveryTour());

    }

    @Test
    void clearMap() {
        //Arrange
        long id1 = 1;
        long id2 = 2;
        Intersection i1 = new Intersection();
        Intersection i2 = new Intersection();
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        HashMap<Long, Intersection> blankListIntersection = new HashMap<>();
        listIntersection.put(id1, i1);
        listIntersection.put(id2, i2);
        Map m = new Map(listIntersection);
        Path p = new Path();
        ArrayList<Path> listPath = new ArrayList<>();
        ArrayList<Path> blankListPath = new ArrayList<>();
        listPath.add(p);
        m.getDeliveryTour().setListPaths(listPath);
        //Act
        m.clearMap();
        //Assert
        assertEquals(blankListIntersection,m.getListIntersections());
        assertEquals(blankListPath,m.getDeliveryTour().getListPaths());

    }

    @Test
    void clearRequests() {
        //Arrange
        Request r1 = new Request();
        Request r2 = new Request();
        ArrayList<Request> listRequest = new ArrayList<>();
        ArrayList<Request> blankListRequest = new ArrayList<>();
        listRequest.add(r1);
        listRequest.add(r2);
        Map m = new Map();
        m.setListRequest(listRequest);
        //Act
        m.clearRequests();
        //Assert
        assertEquals(blankListRequest,m.getListRequests());
    }

    @Test
    void findMinLat() {
        //Arrange
        Intersection i1 = new Intersection(1,10,3);
        Intersection i2 = new Intersection(2, 5,3 );
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(1l, i1);
        listIntersection.put(2l, i2);
        Map m = new Map(listIntersection);
        //Act
        Double d = m.getMinLat();
        //Assert
        assertEquals(5,d);
    }

    @Test
    void findMaxLat() {
        //Arrange
        Intersection i1 = new Intersection(1,5,3);
        Intersection i2 = new Intersection(2, 10,3 );
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(1l, i1);
        listIntersection.put(2l, i2);
        Map m = new Map(listIntersection);
        //Act
        Double d = m.getMaxLat();
        //Assert
        assertEquals(10,d);
    }

    @Test
    void findMinLong() {
        //Arrange
        Intersection i1 = new Intersection(1,10,10);
        Intersection i2 = new Intersection(2, 10,5 );
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(1l, i1);
        listIntersection.put(2l, i2);
        Map m = new Map(listIntersection);
        //Act
        Double d = m.getMinLong();
        //Assert
        assertEquals(5,d);
    }

    @Test
    void findMaxLong() {
        //Arrange
        Intersection i1 = new Intersection(1,10,5);
        Intersection i2 = new Intersection(2, 10,10);
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(1l, i1);
        listIntersection.put(2l, i2);
        Map m = new Map(listIntersection);
        //Act
        Double d = m.getMaxLong();
        //Assert
        assertEquals(10,d);
    }
    @Test
    void getTourStopByIdForPickUp() {
        //Arrange
        PickUpPoint p1 = new PickUpPoint(1,2,3,4);
        DeliveryPoint d1 = new DeliveryPoint(2,2,3,4);
        Request r1 = new Request(p1,d1);
        ArrayList<Request> listRequest = new ArrayList<>();
        listRequest.add(r1);
        Map m = new Map();
        m.setListRequest(listRequest);
        //Act
        Intersection i = m.getTourStopById(1);
        //Assert
        assertEquals(p1,i);
    }
    @Test
    void getTourStopByIdForDelivery() {
        //Arrange
        PickUpPoint p1 = new PickUpPoint(1,2,3,4);
        DeliveryPoint d1 = new DeliveryPoint(2,2,3,4);
        Request r1 = new Request(p1,d1);
        ArrayList<Request> listRequest = new ArrayList<>();
        listRequest.add(r1);
        Map m = new Map();
        m.setListRequest(listRequest);
        //Act
        Intersection i = m.getTourStopById(2);
        //Assert
        assertEquals(d1,i);
    }

    @Test
    void getRequestByIntersectionId() {
        //Arrange
        PickUpPoint p1 = new PickUpPoint(1,2,3,4);
        DeliveryPoint d1 = new DeliveryPoint(2,2,3,4);
        Request r1 = new Request(p1,d1);
        ArrayList<Request> listRequest = new ArrayList<>();
        listRequest.add(r1);
        Map m = new Map();
        m.setListRequest(listRequest);
        //Act
        Request i = m.getRequestByIntersectionId(1);
        //Assert
        assertEquals(r1,i);
    }
    @Test
    void setListRequestWorking() {
        //Arrange
        PickUpPoint p1 = new PickUpPoint(1,2,3,4);
        DeliveryPoint d1 = new DeliveryPoint(2,2,3,4);
        Request r1 = new Request(p1,d1);
        ArrayList<Request> listRequest = new ArrayList<>();
        listRequest.add(r1);
        Map m = new Map();
        m.setListRequest(listRequest);
        //Act
        Request i = m.getRequestByIntersectionId(1);
        //Assert
        assertEquals(r1,i);
    }
    @Test
    void setListIntersections() {
        //Arrange
        Intersection i1 = new Intersection(1,10,5);
        Intersection i2 = new Intersection(2, 10,10);
        HashMap<Long, Intersection> listIntersection = new HashMap<>();
        listIntersection.put(1l, i1);
        listIntersection.put(2l, i2);
        Map m = new Map();
        //Act
        m.setListIntersections(listIntersection);
        //Assert
        assertEquals(listIntersection,m.getListIntersections());
    }
    @Test
    void setDepot() {
        //Arrange
        Depot d = new Depot(1,"1");
        Map m = new Map();
        //Act
        m.setDepot(d);
        //Assert
        assertEquals(d,m.getDepot());
    }
    @Test
    void getTour() {
        //Arrange
        Map m = new Map();
        Tour t = new Tour(m);
        //Act
        m.setDeliveryTour(t);
        //Assert
        assertEquals(t,m.getDeliveryTour());
    }





}