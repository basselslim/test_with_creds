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
                        long id = Long.parseLong(att.item(0).getNodeValue());
                        double latitude = Double.parseDouble(att.item(1).getNodeValue());
                        double longitude = Double.parseDouble(att.item(2).getNodeValue());
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
                        long destination = Long.parseLong(att.item(0).getNodeValue());
                        double length = Double.parseDouble(att.item(1).getNodeValue());
                        String name = att.item(2).getNodeValue();
                        long origin = Long.parseLong(att.item(3).getNodeValue());
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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        HashMap<Long, Intersection> mapIntersection = new HashMap<Long, Intersection>();

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
                ArrayList<Segment> listSegment = new ArrayList<Segment>();

                if(n.getAttributes() != null && n.getAttributes().getLength() > 0){

                    NamedNodeMap att = n.getAttributes();
                    int nbAtt = att.getLength();

                    if(nodeName == "intersection")
                    {
                        long id = Long.parseLong(att.item(0).getNodeValue());
                        double latitude = Double.parseDouble(att.item(1).getNodeValue());
                        double longitude = Double.parseDouble(att.item(2).getNodeValue());
                        //Intersection intersection = new Intersection(id, latitude, longitude,listSegment);
                    }

                    if(nodeName == "segment")
                    {
                        long destination = Long.parseLong(att.item(0).getNodeValue());
                        double length = Double.parseDouble(att.item(1).getNodeValue());
                        String name = att.item(2).getNodeValue();
                        long origin = Long.parseLong(att.item(3).getNodeValue());

                        //Segment segment = new Segment(length, name, destination);
                        //listSegment.add(segment);

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