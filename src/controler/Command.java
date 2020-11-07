package controler;

/**
 * Command
 *
 * @author T-REXANOME
 */
public interface Command {

    /**
     * Execute the command this
     */
    void doCommand();

    /**
     * Execute the reverse command of this
     */
    void undoCommand();
}
