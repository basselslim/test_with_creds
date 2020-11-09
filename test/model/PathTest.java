package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

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
        Step d= new Step();
        Step a= new Step();
        ArrayList<Segment> list = new ArrayList<Segment>();
        Segment s1 = new Segment(10,"Rue test",2);
        list.add(s1);
        Segment s2 = new Segment(10,"Rue test",3);
        list.add(s1);
        //Act
        Path p = new Path(list,d, a);
        //Assert
        assertEquals(list,p.getListSegments());
        assertEquals(d,p.getDeparture());
        assertEquals(a,p.getArrival());
        assertEquals(20,p.getPathLength());

    }

    @Test
    void constructorFullWorkingWithListNull()
    {
        //Arrange
        Step d= new Step();
        Step a= new Step();
        LinkedList<Segment> list = new LinkedList<Segment>();
        //Act
        Path p = new Path(null,d, a);
        //Assert
        assertEquals(list,p.getListSegments());
        assertEquals(d,p.getDeparture());
        assertEquals(a,p.getArrival());
        assertEquals(0,p.getPathLength());

    }
    @Test
    void getDuration(){
        //Arrange
        Path p = new Path();
        p.setPathLength(150);
        //Act
        int d = p.getDuration();
        //Assert
        assertEquals(36,d);
    };

    @Test
    void getIdDeparture(){
        //Arrange
        PickUpPoint d= new PickUpPoint(1,2,3,4);
        Step a= new Step();
        ArrayList<Segment> list = new ArrayList<Segment>();
        //Act
        Path p = new Path(list,d, a);
        //Assert
        assertEquals(1,p.getIdDeparture());
    }
    @Test
    void getIdArrival(){
        //Arrange
        PickUpPoint a= new PickUpPoint(1,2,3,4);
        Step d= new Step();
        ArrayList<Segment> list = new ArrayList<Segment>();
        //Act
        Path p = new Path(list,d, a);
        //Assert
        assertEquals(1,p.getIdArrival());
    }
}