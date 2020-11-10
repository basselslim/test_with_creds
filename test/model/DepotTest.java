package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepotTest {
    @Test
    void emptyConstructorIsNotNull()
    {
        //Arrange

        //Act
        Depot d = new Depot();
        //Assert
        assertNotNull(d);
    }
    @Test
    void constructorCorrect()
    {
        //Arrange
        long id = 1;
        String departureTime= "2";
        //Act
        Depot d = new Depot(id,departureTime);
        //Assert
        assertNotNull(d);
        assertEquals(id,d.getId());
        assertEquals(departureTime,d.getDepartureTime());
    }
}