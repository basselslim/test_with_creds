package controler;

import model.Map;
import model.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReverseCommandTest {
    ReverseCommand reverseCommand;
    Command command;
    Map m;
    Request r;
    @BeforeEach
    void setUp() {
        m = new Map();
        r = new Request();
        command = new AddCommand(m,r);
        command.doCommand();
        reverseCommand = new ReverseCommand(command);
    }

    @Test
    void doCommand() {
        reverseCommand.doCommand();
        assert (m.getListRequests().size() == 0);
    }

    @Test
    void undoCommand() {
        reverseCommand.undoCommand();
        assert (m.getListRequests().size() == 2);
    }
}