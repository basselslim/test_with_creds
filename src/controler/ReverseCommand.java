package controler;

/**
 * Create the command reverse to cmd (so that cmd.doCommand corresponds to this.undoCommand, and vice-versa)
 *
 * @author T-REXANOME
 */
public class ReverseCommand implements Command {
    private Command cmd;

    /**
     * Constructor
     *
     * @param cmd the command to reverse
     */
    public ReverseCommand(Command cmd) {
        this.cmd = cmd;
    }

    /**
     * Do.
     */
    @Override
    public int doCommand() {
        return cmd.undoCommand();
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     */
    @Override
    public int undoCommand() {
        return cmd.doCommand();
    }
}
