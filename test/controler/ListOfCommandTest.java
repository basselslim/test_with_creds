package controler;

import model.Map;
import model.Request;
import model.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListOfCommandTest {
    ListOfCommand listOfCommand;
    Map m;
    Request r;
    Step idPickUp;
    Step idDelivery;
    @BeforeEach
    void setUp() {
        listOfCommand = new ListOfCommand();
        m = new Map();
        r = new Request();
    }

    @Test
    void add() {
        AddCommand c = new AddCommand(m,r,idPickUp,idDelivery);
        listOfCommand.add(c);
        assert (listOfCommand.currentIndex == 0);
    }

    @Test
    void undo() {
        AddCommand c = new AddCommand(m,r,idPickUp,idDelivery);
        listOfCommand.add(c);
        listOfCommand.undo();
        assert (listOfCommand.currentIndex == -1);
        assert (listOfCommand.list.size() == 1);
    }

    @Test
    void cancel() {
        AddCommand c = new AddCommand(m,r,idPickUp,idDelivery);
        listOfCommand.add(c);
        listOfCommand.cancel();
        assert (listOfCommand.currentIndex == -1);
        assert (listOfCommand.list.size() == 0);
    }

    @Test
    void redo() {
        AddCommand c = new AddCommand(m,r,idPickUp,idDelivery);
        listOfCommand.add(c);
        listOfCommand.undo();
        listOfCommand.redo();
        assert (listOfCommand.currentIndex == 0);
    }

    @Test
    void reset() {
        AddCommand c = new AddCommand(m,r,idPickUp,idDelivery);
        listOfCommand.add(c);
        listOfCommand.reset();
        assert (listOfCommand.currentIndex == -1);
        assert (listOfCommand.list.size() == 0);
    }
}