package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StepTest {

    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Step s = new Step();
        //Assert
        assertNotNull(s);
    }
    @Test
    void constructorFullWorking()
    {
        //Arrange
        long id = 1;
        double latitude= 2;
        double longitude= 3;
        //Act
        Step s = new Step(id,latitude,longitude);
        //Assert
        assertEquals(id,s.getId());
        assertEquals(latitude,s.getLatitude());
        assertEquals(longitude,s.getLongitude());

    }

    @Test
    void getRequest() {
        //Arrange
        Request r = new Request();
        Step s = new Step();
        s.setRequest(r);
        //Act
        Request test = s.getRequest();
        //Assert
        assertEquals(r,test);
    }
}