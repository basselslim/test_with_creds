package controler;

import model.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class XMLLoader {

    /**
     * Default constructor
     */
    public XMLLoader() {
    }

    public void parseMapXML(String pathNameXMLFile, Map map){

        map.clearMap();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();


        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File fileXML = new File(pathNameXMLFile);
            Document xml;

            xml = builder.parse(fileXML);
            Element root = xml.getDocumentElement();

            NodeList nodes = root.getChildNodes();
            int nbNode = nodes.getLength();
            for(int i = 0; i < nbNode; i++){
                Node n = nodes.item(i);
                String nodeName = n.getNodeName();

                if(n.getAttributes() != null && n.getAttributes().getLength() > 0){

                    NamedNodeMap att = n.getAttributes();

                    if(nodeName == "intersection")
                    {
                        ArrayList<Segment> listSegmentsV1 = new ArrayList<Segment>();
                        long id = Long.parseLong(att.getNamedItem("id").getNodeValue());
                        double latitude = Double.parseDouble(att.getNamedItem("latitude").getNodeValue());
                        double longitude = Double.parseDouble(att.getNamedItem("longitude").getNodeValue());
                        Intersection intersection = new Intersection(id, latitude, longitude, listSegmentsV1);
                        map.getListIntersections().put(id, intersection);


                    }
                }
            }

            for(int i = 0; i < nbNode; i++){
                Node n = nodes.item(i);
                String nodeName = n.getNodeName();

                if(n.getAttributes() != null && n.getAttributes().getLength() > 0){

                    NamedNodeMap att = n.getAttributes();

                    if(nodeName == "segment")
                    {
                        ArrayList<Segment> listSegments = new ArrayList<Segment>();
                        long destination = Long.parseLong(att.getNamedItem("destination").getNodeValue());
                        double length = Double.parseDouble(att.getNamedItem("length").getNodeValue());
                        String name = att.getNamedItem("name").getNodeValue();
                        long origin = Long.parseLong(att.getNamedItem("origin").getNodeValue());
                        Segment segment = new Segment(length, name, destination);

                        Intersection intersection = map.getListIntersections().get(origin);
                        intersection.getListSegments().add(segment);
                        map.getListIntersections().replace(origin, intersection);
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void parseRequestXML(String pathNameXMLFile, Map map){

        map.clearRequests();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File fileXML = new File(pathNameXMLFile);
            Document xml;

            xml = builder.parse(fileXML);
            Element root = xml.getDocumentElement();

            NodeList nodes = root.getChildNodes();
            int nbNode = nodes.getLength();
            for(int i = 0; i < nbNode; i++){
                Node n = nodes.item(i);
                String nodeName = n.getNodeName();

                if(n.getAttributes() != null && n.getAttributes().getLength() > 0){
                    NamedNodeMap att = n.getAttributes();
                    int nbAtt = att.getLength();

                    if(nodeName == "request")
                    {
                        long pickUpAdress = Long.parseLong(att.getNamedItem("pickupAddress").getNodeValue());
                        System.out.println("PickUp Adress "+pickUpAdress);
                        long deliveryAdress = Long.parseLong(att.getNamedItem("deliveryAddress").getNodeValue());
                        System.out.println("Delivery Adress "+deliveryAdress);
                        int pickUpDuration = Integer.parseInt(att.getNamedItem("pickupDuration").getNodeValue());
                        System.out.println("PU duration "+pickUpDuration);
                        int deliveryDuration = Integer.parseInt(att.getNamedItem("deliveryDuration").getNodeValue());
                        System.out.println("delivery duration "+deliveryDuration);
                        Intersection pickupIntersection = map.getListIntersections().get(pickUpAdress);
                        Intersection deliveryIntersection = map.getListIntersections().get(deliveryAdress);
                        PickUpPoint pickUpPoint = new PickUpPoint(pickupIntersection.getId(),pickupIntersection.getLatitude(),pickupIntersection.getLongitude(),pickUpDuration);
                        DeliveryPoint deliveryPoint = new DeliveryPoint(deliveryIntersection.getId(),deliveryIntersection.getLatitude(),deliveryIntersection.getLongitude(),deliveryDuration);
                        Request request = new Request(pickUpPoint,deliveryPoint);
                        map.getListRequests().add(request);
                    }

                    if(nodeName == "depot")
                    {
                        long adress = Long.parseLong(att.getNamedItem("address").getNodeValue());
                        System.out.println("Depot Adress "+adress);
                        String departureTime = att.getNamedItem("departureTime").getNodeValue();
                        System.out.println("Depot departure "+departureTime);
                        Depot depot = new Depot(adress,departureTime);
                        map.setDepot(depot);

                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*Map mapParsed = new Map(listIntersection, listSegment);
        System.out.println("Map créée");
        mapParsed.display();
        return mapParsed;*/
    }
}