package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionTest {

    @Test
    void constructorWithoutListWorking()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        ArrayList<Segment> list = new ArrayList<Segment>();
        //Act
        Intersection i = new Intersection(id,latitude,longitude);
        //Assert
        assertEquals(id,i.getId());
        assertEquals(latitude,i.getLatitude());
        assertEquals(longitude,i.getLongitude());
        assertEquals(list,i.getListSegments());
        assertNull(i.getPrevious());
        assertEquals(0.0,i.getRouteScore());
        assertEquals(0.0,i.getEstimatedScore());
    }
    @Test
    void constructorFullWorking()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Segment s1 = new Segment();
        list.add(s1);
        //Act
        Intersection i = new Intersection(id,latitude,longitude,list);
        //Assert
        assertEquals(id,i.getId());
        assertEquals(latitude,i.getLatitude());
        assertEquals(longitude,i.getLongitude());
        assertEquals(list,i.getListSegments());
        assertNull(i.getPrevious());
        assertEquals(0.0,i.getRouteScore());
        assertEquals(0.0,i.getEstimatedScore());

    }
    @Test
    void constructorWithAnotherIntersectionWorking()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Intersection i = new Intersection(id,latitude,longitude,list);
        //Act
        Intersection j = new Intersection(i);
        //Assert
        assertEquals(i.getId(),j.getId());
        assertEquals(i.getLatitude(),j.getLatitude());
        assertEquals(i.getLongitude(), j.getLongitude());
        assertEquals(i.getListSegments(), j.getListSegments());
        assertNull(i.getPrevious());
        assertEquals(0.0,i.getRouteScore());
        assertEquals(0.0,i.getEstimatedScore());
    }
    @Test
    void constructorWithCurrentAndPreviousIntersectionWorking()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        long id1 = 2;
        double latitude1 = 3.3;
        double longitude1 = 4.4;
        double routeScore = 5.5;
        double estimatedScore = 6.6;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Intersection i = new Intersection(id,latitude,longitude,list);
        Intersection j = new Intersection(id1,latitude1,longitude1);
        //Act
        Intersection h = new Intersection(i,j,routeScore,estimatedScore);
        //Assert
        assertEquals(i.getId(),h.getId());
        assertEquals(i.getLatitude(),h.getLatitude());
        assertEquals(i.getLongitude(), h.getLongitude());
        assertEquals(i.getListSegments(), h.getListSegments());
        assertEquals(j,h.getPrevious());
        assertEquals(routeScore,h.getRouteScore());
        assertEquals(estimatedScore,h.getEstimatedScore());
    }

    @Test
    void compareWhenActualEstimatedScoreIsSmaller() {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        long id1 = 2;
        double latitude1 = 3.3;
        double longitude1 = 4.4;
        double estimatedScoreA = 0.0;
        double estimatedScoreB =  1.1;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Intersection i = new Intersection(id,latitude,longitude);
        Intersection j = new Intersection(id1,latitude1,longitude1);
        i.setEstimatedScore(estimatedScoreA);
        j.setEstimatedScore(estimatedScoreB);
        //Act
        int comparison = i.compareTo(j);
        //Assert
        assertEquals(-1,comparison);
    }
    @Test
    void compareWhenActualEstimatedScoreIsGreater() {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        long id1 = 2;
        double latitude1 = 3.3;
        double longitude1 = 4.4;
        double estimatedScoreA = 0.0;
        double estimatedScoreB =  1.1;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Intersection i = new Intersection(id,latitude,longitude);
        Intersection j = new Intersection(id1,latitude1,longitude1);
        i.setEstimatedScore(estimatedScoreA);
        j.setEstimatedScore(estimatedScoreB);
        //Act
        int comparison = j.compareTo(i);
        //Assert
        assertEquals(1,comparison);
    }
    @Test
    void compareWhenBothEstimatedScoreAreEqual() {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        long id1 = 2;
        double latitude1 = 3.3;
        double longitude1 = 4.4;
        double estimatedScoreA = 0.0;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Intersection i = new Intersection(id,latitude,longitude);
        Intersection j = new Intersection(id1,latitude1,longitude1);
        i.setEstimatedScore(estimatedScoreA);
        j.setEstimatedScore(estimatedScoreA);
        //Act
        int comparison = i.compareTo(j);
        //Assert
        assertEquals(0,comparison);
    }
}