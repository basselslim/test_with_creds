package controler;

/**
 * Command.
 *
 * @author T-REXANOME
 */
public interface Command {

    /**
     * Execute the command this.
     *
     * @return error code
     */
    int doCommand();

    /**
     * Execute the reverse command of this.
     *
     * @return error code
     */
    int undoCommand();
}
