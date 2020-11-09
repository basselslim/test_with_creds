package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Segment s = new Segment();
        //Assert
        assertNotNull(s);
    }
    @Test
    void constructorFullWorking()
    {
        //Arrange
        double length = 10;
        String streetName = "Rue 1";
        long destination = 1;
        //Act
        Segment s1 = new Segment(length,streetName,destination);
        //Assert
        assertEquals(length,s1.getLength());
        assertEquals(streetName,s1.getStreetName());
        assertEquals(destination,s1.getDestination());

    }
}