package model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 
 */
public class Tour extends Observable {
    /**
     *
     */
    protected int tourLength;
    protected List<Path> listPaths;
    protected String roadMapFilePath;

    /**
     * Default constructor
     */
    public Tour() {
    }

    public Tour(List<Path> listPaths) {
        this.listPaths = listPaths;
        this.tourLength = 0;
        for(Path p: listPaths) {
            this.tourLength += p.getPathLength();
        }
    }
    /**
     * Getters - Setters
     */
    public int getTourLength() {
        return tourLength;
    }

    public List<Path> getListPaths() {
        return listPaths;
    }

    public String generateTextForRoadMap() {
        String totalText = "Roadmap \n\n\n";
        int i = 0;
        for(Path p: listPaths) {
            i++;
            String PathTitle = "Path n°" + i + ": from intersection " + p.idDeparture + " to intersection " + p.idArrival + ":\n\n";
            totalText+=PathTitle;
            int j = 0;
            for(Segment s: p.getListSegments()) {
                j++;
                String SegmentDescription = "   "+ i +"."+j+": Take " + s.getStreetName() + " in direction of " + s.getDestination() + "\n\n";
                totalText+=SegmentDescription;
            }
            totalText+="\n";

        }
        return totalText;
    }

    public void generateRoadMap(String path)
    {
        try {
            File roadMap = new File(path);
            if (roadMap.createNewFile()) {
                System.out.println("File created: " + roadMap.getName());
                System.out.println("Absolute path: " + roadMap.getAbsolutePath());
                this.roadMapFilePath = roadMap.getAbsolutePath();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(this.roadMapFilePath);
            myWriter.write(this.generateTextForRoadMap());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}