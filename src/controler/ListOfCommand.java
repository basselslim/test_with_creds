package controler;

import java.util.LinkedList;

/**
 * @author T-REXANOME
 */
public class ListOfCommand {
    protected LinkedList<Command> list;
    protected int currentIndex;

    /**
     * Default constructor.
     */
    public ListOfCommand() {
        currentIndex = -1;
        list = new LinkedList<Command>();
    }

    /**
     * Add command c to this.
     *
     * @param c the command to add
     * @return error code
     */
    public int add(Command c){
        int i = currentIndex+1;
        while(i<list.size()){
            list.remove(i);
        }
        currentIndex++;
        list.add(currentIndex, c);
        int errorCode = c.doCommand();
        return errorCode;
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo).
     */
    public void undo() {
        if (currentIndex >= 0) {
            Command cde = list.get(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Permanently remove the last added command (this command cannot be reinserted again with redo).
     */
    public void cancel() {
        if (currentIndex >= 0) {
            Command cde = list.get(currentIndex);
            list.remove(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Reinsert the last command removed by undo.
     */
    public int redo(){
        int errorCode = 0;
        if (currentIndex < list.size()-1){
            currentIndex++;
            Command cde = list.get(currentIndex);
            errorCode = cde.doCommand();
        }
        return errorCode;
    }

    /**
     * Permanently remove all commands from the list.
     */
    public void reset() {
        currentIndex = -1;
        list.clear();
    }
}
