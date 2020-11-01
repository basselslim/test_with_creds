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

}