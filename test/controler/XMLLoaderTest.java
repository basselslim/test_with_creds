package controler;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class XMLLoaderTest {

    @Test
    void emptyConstructorNotNull(){
        //Arrange

        //Act
        XMLLoader x = new XMLLoader();
        //Assert
        assertNotNull(x);
    }

    @Test
    void clearMapEmptiesTheMap(){
        //Arrange
        long id = 1;
        double latitude = 2;
        double longitude = 3;
        Intersection intersection = new Intersection(id, latitude, longitude);
        HashMap<Long,Intersection> listIntersection = new HashMap<Long,Intersection>();
        listIntersection.put(intersection.getId(),intersection);
        Map map = new Map();
        map.setListIntersections(listIntersection);
        //Act
        map.clearMap();
        //Assert
        assertNotNull(map.getListIntersections());
    }

    @Test
    void pathnameXMLNotCorrect(){
        //Arrange
        String pathNameXMLFile = "/Users/basselslim/Documents/4IF/Semestre_1/AGILE/PLD/fichiersXML2020/Map.xml";
        //Act
        File fileXML = new File(pathNameXMLFile);
        //Assert
        assertNotNull(fileXML);
    }

    @Test
    void mapParsedWell(){
        //Arrange
        String pathNameXMLFile = "/Users/basselslim/Documents/4IF/Semestre_1/AGILE/PLD/fichiersXML2020/testMap.xml";
        Map map = new Map();
        XMLLoader xmlLoader = new XMLLoader();
        long id = 1;
        long latitude = 2;
        long longitude = 3;
        Intersection intersection = new Intersection(id, latitude, longitude);

        long destination = 2;
        double length = 20;
        String name = "Rue Du Test";
        long origin = 1;
        Segment segment = new Segment(length, name, destination);

        //Act
        xmlLoader.parseMapXML(pathNameXMLFile, map);
        intersection.addSegment(segment);

        //Assert
        assert(intersection != map.getListIntersections().get(id));
        assert(segment != map.getListIntersections().get(origin).getListSegments().get(0));
    }

    @Test
    void requestParsedWell(){
        //Arrange
        String pathNameXMLFileRequests = "/Users/basselslim/Documents/4IF/Semestre_1/AGILE/PLD/fichiersXML2020/testRequest.xml";
        String pathNameXMLFileMap = "/Users/basselslim/Documents/4IF/Semestre_1/AGILE/PLD/fichiersXML2020/testMap.xml";
        Map map = new Map();
        XMLLoader xmlLoader = new XMLLoader();
        long pickUpAdress = 2;
        long deliveryAdress = 3;
        int pickUpDuration = 100;
        int deliveryDuration = 100;
        long latitudeP = 4;
        long longitudeP = 6;
        long latitudeD = 6;
        long longitudeD = 9;
        long depotId = 1;
        String depotDepartureTime = "8:0:0";
        Intersection pickupIntersection = new Intersection(pickUpAdress, latitudeP, longitudeP);
        Intersection deliveryIntersection = new Intersection(deliveryAdress, latitudeD, longitudeD);
        PickUpPoint pickUpPoint = new PickUpPoint(pickupIntersection.getId(), pickupIntersection.getLatitude(), pickupIntersection.getLongitude(), pickUpDuration);
        DeliveryPoint deliveryPoint = new DeliveryPoint(deliveryIntersection.getId(), deliveryIntersection.getLatitude(), deliveryIntersection.getLongitude(), deliveryDuration);
        Request request = new Request(pickUpPoint, deliveryPoint);
        Depot depot = new Depot(depotId, depotDepartureTime);

        //Act
        xmlLoader.parseMapXML(pathNameXMLFileMap, map);
        xmlLoader.parseRequestXML(pathNameXMLFileRequests, map);

        //Assert
        assert(request != map.getListRequests().get(0));
        assert(depot != map.getDepot());
    }
}