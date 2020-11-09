package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryPointTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        DeliveryPoint d = new DeliveryPoint();
        //Assert
        assertNotNull(d);
    }
    @Test
    void standardConstructorCorrect()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        int deliveryDuration = 2;
        Intersection i = new Intersection(id,latitude,longitude);
        //Act
        DeliveryPoint d = new DeliveryPoint(i,deliveryDuration);
        //Assert
        assertNotNull(d);
        assertEquals(id,d.getId());
        assertEquals(latitude,d.getLatitude());
        assertEquals(longitude,d.getLongitude());
        assertEquals(deliveryDuration,d.getDeliveryDuration());
    }
    @Test
    void constructorCorrectWithExtensionOfIntersection()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        int deliveryDuration = 2;
        //Act
        DeliveryPoint d = new DeliveryPoint(id,latitude,longitude,deliveryDuration);
        //Assert
        assertNotNull(d);
        assertEquals(id,d.getId());
        assertEquals(latitude,d.getLatitude());
        assertEquals(longitude,d.getLongitude());
        assertEquals(deliveryDuration,d.getDeliveryDuration());
    }
    @Test
    void setDeliveryDurationWorking(){
        //Arrange
        DeliveryPoint d = new DeliveryPoint();
        //Act
        d.setDeliveryDuration(2);
        //Assert
        assertEquals(2,d.getDeliveryDuration());
    }

    @Test
    void getRequest(){
        //Arrange
        DeliveryPoint d = new DeliveryPoint();
        Request r1 = new Request();
        d.setRequest(r1);
        //Act
        Request r2 = d.getRequest();
        //Assert
        assertEquals(r1,r2);
    }

}