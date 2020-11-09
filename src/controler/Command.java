package controler;

/**
 * Command.
 *
 * @author T-REXANOME
 */
public interface Command {

    /**
     * Execute the command this.
     */
    int doCommand();

    /**
     * Execute the reverse command of this.
     */
    int undoCommand();
}
