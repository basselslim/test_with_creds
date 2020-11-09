package controler;

import model.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Load XML files.
 *
 * @author T-REXANOME
 */
public class XMLLoader {

    /**
     * Default constructor.
     */
    public XMLLoader() {
    }

    /**
     *
     * @param pathNameXMLFile
     * @param map
     * @return
     */
    public int parseMapXML(String pathNameXMLFile, Map map) {
        int check = 0;
        HashMap<Long,Intersection> listIntersection = new HashMap<Long,Intersection>();
        map.clearMap();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File fileXML = new File(pathNameXMLFile);
            Document xml;

            xml = builder.parse(fileXML);
            Element root = xml.getDocumentElement();

            if(!root.getTagName().equals("map")){
                check++;
            }
            else{
                NodeList nodes = root.getChildNodes();
                int nbNode = nodes.getLength();

                for (int i = 1; i < nbNode; i = i+2) {
                    Node n = nodes.item(i);
                    String nodeName = n.getNodeName();

                    if(!nodeName.equals("intersection") && !nodeName.equals("segment")){
                        check++;
                        break;
                    }
                    else{
                        if (n.getAttributes() != null) {

                            NamedNodeMap att = n.getAttributes();

                            if (nodeName == "intersection") {
                                if(n.getAttributes().getLength() != 3 || !n.getAttributes().item(0).getNodeName().equals("id") || !n.getAttributes().item(1).getNodeName().equals("latitude") || !n.getAttributes().item(2).getNodeName().equals("longitude")){
                                    check++;
                                    break;
                                }
                                else{
                                    ArrayList<Segment> listSegmentsV1 = new ArrayList<Segment>();
                                    long id = Long.parseLong(att.getNamedItem("id").getNodeValue());
                                    double latitude = Double.parseDouble(att.getNamedItem("latitude").getNodeValue());
                                    double longitude = Double.parseDouble(att.getNamedItem("longitude").getNodeValue());
                                    Intersection intersection = new Intersection(id, latitude, longitude, listSegmentsV1);
                                    listIntersection.put(intersection.getId(),intersection);
                                }
                            }
                        }
                    }
                }

                for (int i = 1; i < nbNode; i = i+2) {
                    Node n = nodes.item(i);
                    String nodeName = n.getNodeName();

                    if(!nodeName.equals("intersection") && !nodeName.equals("segment")){
                        check++;
                        break;
                    }
                    else{
                        if (n.getAttributes() != null) {

                            NamedNodeMap att = n.getAttributes();

                            if (nodeName == "segment") {
                                if(n.getAttributes().getLength() != 4 || !n.getAttributes().item(0).getNodeName().equals("destination") || !n.getAttributes().item(1).getNodeName().equals("length") || !n.getAttributes().item(2).getNodeName().equals("name") || !n.getAttributes().item(3).getNodeName().equals("origin")){
                                    check++;
                                    break;
                                }
                                else{
                                    ArrayList<Segment> listSegments = new ArrayList<Segment>();
                                    long destination = Long.parseLong(att.getNamedItem("destination").getNodeValue());
                                    double length = Double.parseDouble(att.getNamedItem("length").getNodeValue());
                                    String name = att.getNamedItem("name").getNodeValue();
                                    long origin = Long.parseLong(att.getNamedItem("origin").getNodeValue());
                                    Segment segment = new Segment(length, name, destination);

                                    Intersection intersection = listIntersection.get(origin);
                                    intersection.addSegment(segment);
                                }
                            }
                        }
                    }
                }
            }
            map.setListIntersections(listIntersection);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(check);
        return check;
    }

    /**
     *
     * @param pathNameXMLFile
     * @param map
     * @return
     */
    public int parseRequestXML(String pathNameXMLFile, Map map) {
        int check = 0;

        map.clearRequests();
        map.clearTour();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Request> listRequest = new ArrayList<Request>();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            File fileXML = new File(pathNameXMLFile);
            Document xml;

            xml = builder.parse(fileXML);
            Element root = xml.getDocumentElement();

            if(!root.getTagName().equals("planningRequest")){
                check++;
            }
            else{
                NodeList nodes = root.getChildNodes();
                int nbNode = nodes.getLength();
                for (int i = 1; i < nbNode; i = i + 2) {
                    Node n = nodes.item(i);
                    String nodeName = n.getNodeName();
                    if(!nodeName.equals("depot") && !nodeName.equals("request")){
                        check++;
                        break;
                    }
                    else
                    {
                        if (n.getAttributes() != null && n.getAttributes().getLength() > 0) {
                            NamedNodeMap att = n.getAttributes();
                            int nbAtt = att.getLength();
                            if (nodeName == "request") {
                                if(n.getAttributes().getLength() != 4 || !n.getAttributes().item(0).getNodeName().equals("deliveryAddress") || !n.getAttributes().item(1).getNodeName().equals("deliveryDuration") || !n.getAttributes().item(2).getNodeName().equals("pickupAddress") || !n.getAttributes().item(3).getNodeName().equals("pickupDuration")){
                                    check++;
                                    break;
                                }
                                else{
                                    long pickUpAdress = Long.parseLong(att.getNamedItem("pickupAddress").getNodeValue());
                                    long deliveryAdress = Long.parseLong(att.getNamedItem("deliveryAddress").getNodeValue());
                                    int pickUpDuration = Integer.parseInt(att.getNamedItem("pickupDuration").getNodeValue());
                                    int deliveryDuration = Integer.parseInt(att.getNamedItem("deliveryDuration").getNodeValue());
                                    Intersection pickupIntersection = map.getListIntersections().get(pickUpAdress);
                                    Intersection deliveryIntersection = map.getListIntersections().get(deliveryAdress);
                                    PickUpPoint pickUpPoint = new PickUpPoint(pickupIntersection.getId(), pickupIntersection.getLatitude(), pickupIntersection.getLongitude(), pickUpDuration);
                                    DeliveryPoint deliveryPoint = new DeliveryPoint(deliveryIntersection.getId(), deliveryIntersection.getLatitude(), deliveryIntersection.getLongitude(), deliveryDuration);
                                    Request request = new Request(pickUpPoint, deliveryPoint);
                                    listRequest.add(request);
                                }
                            }

                            if (nodeName == "depot") {
                                if(n.getAttributes().getLength() != 2 || !n.getAttributes().item(0).getNodeName().equals("address") || !n.getAttributes().item(1).getNodeName().equals("departureTime")){
                                    check++;
                                    break;
                                }
                                else{
                                    long adress = Long.parseLong(att.getNamedItem("address").getNodeValue());
                                    String departureTime = att.getNamedItem("departureTime").getNodeValue();
                                    Depot depot = new Depot(adress, departureTime);
                                    map.setDepot(depot);
                                }
                            }
                        }
                    }
                }
            }
            map.setListRequest(listRequest);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(check);
        return check;
    }
}