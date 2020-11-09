package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Request r = new Request();
        //Assert
        assertNotNull(r);
    }
    @Test
    void copyConstructorWorking()
    {
        //Arrange
        PickUpPoint p1 = new PickUpPoint(1,2,3,4);
        DeliveryPoint d1 = new DeliveryPoint(2,2,3,4);
        Request r1 = new Request(p1,d1);
        //Act
        Request r2 = new Request(r1);
        //Assert
        assertEquals(r1.getOrder(),r2.getOrder());
        assertEquals(r1.getPickUpPoint(),r2.getPickUpPoint());
        assertEquals(r1.getDeliveryPoint(),r2.getDeliveryPoint());
    }
    @Test
    void standardConstructorWorking()
    {

    }
}