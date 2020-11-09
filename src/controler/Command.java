package controler;

public interface Command {

    /**
     * Execute the command this
     */
    int doCommand();

    /**
     * Execute the reverse command of this
     */
    int undoCommand();
}
