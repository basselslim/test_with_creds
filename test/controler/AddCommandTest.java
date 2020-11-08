package controler;

import model.Map;
import model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddCommandTest {
    AddCommand addCommand;
    Map m;
    Request r;
    Long idPickUp;
    Long idDelivery;
    @BeforeEach
    void setUp() {
        m = new Map();
        r = new Request();
        addCommand = new AddCommand(m,r,idPickUp,idDelivery);
    }

    @Test
    void doCommand() {
        addCommand.doCommand();
        assert (m.getListRequests().size() == 1);
    }

    @Test
    void undoCommand() {
        addCommand.doCommand();
        addCommand.undoCommand();
        assert (m.getListRequests().size() == 0);
    }
}