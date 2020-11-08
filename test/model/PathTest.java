package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Path d = new Path();
        //Assert
        assertNotNull(d);
    }
    @Test
    void constructorFullWorking()
    {
        //Arrange
        long departure = 0;
        long arrival = 0;
        ArrayList<Segment> list = new ArrayList<Segment>();
        Segment s1 = new Segment(10,"Rue test",2);
        list.add(s1);
        Segment s2 = new Segment(10,"Rue test",3);
        list.add(s1);
        //Act
        Path p = new Path(list,departure, arrival);
        //Assert
        assertEquals(list,p.getListSegments());
        assertEquals(departure,p.getIdDeparture());
        assertEquals(arrival,p.getIdArrival());
        assertEquals(20,p.getPathLength());

    }

    @Test
    void constructorFullWorkingWithEmptyList()
    {
        //Arrange
        long departure = 0;
        long arrival = 0;
        ArrayList<Segment> list = new ArrayList<Segment>();
        //Act
        Path p = new Path(list,departure, arrival);
        //Assert
        assertEquals(list,p.getListSegments());
        assertEquals(departure,p.getIdDeparture());
        assertEquals(arrival,p.getIdArrival());
        assertEquals(0,p.getPathLength());

    }
}