package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PickUpPointTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        PickUpPoint p = new PickUpPoint();
        //Assert
        assertNotNull(p);
    }
    @Test
    void standardConstructorCorrect()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        int pickUpDuration = 2;
        Intersection i = new Intersection(id,latitude,longitude);
        //Act
        PickUpPoint p = new PickUpPoint(i,pickUpDuration);
        //Assert
        assertNotNull(p);
        assertEquals(id,p.getId());
        assertEquals(latitude,p.getLatitude());
        assertEquals(longitude,p.getLongitude());
        assertEquals(pickUpDuration,p.getPickUpDuration());
    }
    @Test
    void constructorCorrectWithExtensionOfIntersection()
    {
        //Arrange
        long id = 1;
        double latitude = 1.1;
        double longitude = 2.2;
        int pickUpDuration = 2;
        //Act
        PickUpPoint p = new PickUpPoint(id,latitude,longitude,pickUpDuration);
        //Assert
        assertNotNull(p);
        assertEquals(id,p.getId());
        assertEquals(latitude,p.getLatitude());
        assertEquals(longitude,p.getLongitude());
        assertEquals(pickUpDuration,p.getPickUpDuration());
    }
    @Test
    void setDeliveryDurationWorking(){
        //Arrange
        PickUpPoint d = new PickUpPoint();
        //Act
        d.setPickUpDuration(2);
        //Assert
        assertEquals(2,d.getPickUpDuration());
    }
}