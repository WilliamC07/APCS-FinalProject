import com.google.api.client.auth.oauth2.Credential;

import java.nio.file.Path;
import java.util.*;

public class CSVRepresentation {
    private final CSVAccess csvAccess;
    private final LinkedList<CSVRow> rows;
    private final ArrayDeque<Command> commands = new ArrayDeque<>();
    private final HandleCommand handleCommand = new HandleCommand(this);
    private final CommandBuilder commandBuilder = new CommandBuilder(handleCommand);
    private final Head head;

    public CSVRepresentation(Path pathToCSV, Head head){
        this.head = head;
        this.csvAccess = new CSVAccess(pathToCSV);
        this.rows = csvAccess.readCSV();
    }

    public CSVRepresentation(Credential credential, String sheetID, Head head){
        this.head = head;
        this.csvAccess = new CSVAccess(credential, sheetID);
        this.rows = csvAccess.readCSV();
    }

    public synchronized String getValue(int column, int row){
        try{
            return rows.get(row).get(column).getData();
        }catch(IndexOutOfBoundsException e){
            // This means the cell doesn't exists, so we can just return an empty string
            return "";
        }
    }

    /**
     * Saves the CSV file onto disk using the original location provided by the user {@link #pathToCSV}.
     * TODO: Handle what happens if it can't be saved
     */
    public void save(){
        csvAccess.saveCSV(rows);
    }

    /**
     * Add a command done by the user onto the stack and do those edits onto the screen. Also tells the screen to
     * update to the lastest changes.
     * @param command
     */
    public synchronized void pushCommand(Command command){
        // Add the command to the stack for undo
        commands.push(command);
        // Make the edit happen
        update(command.getColumn(), command.getRow(), command.getNewValue());

        // Screen update
        head.updateScreen();
    }

    /**
     * Update the cell at the given row and column to the new value provided. This will create any preceding row(s) or
     * previous column(s).
     * @param col Column of the cell to update the value
     * @param row Row of the cell to update the value
     * @param value New value to put in the cell
     */
    private void update(int col, int row, String value){
        while(row >= rows.size()){
            rows.add(CSVRow.createEmptyRow());
        }
        rows.get(row).set(col, CSVNode.newInstance(value));

        // Need to update the google sheet if we are connected
        if(head.isConnectedToGoogle()){
            csvAccess.updateToGoogle(col, row, convertToListListObject(rows));
            System.exit(1);
        }
    }

    private List<List<Object>> convertToListListObject(LinkedList<CSVRow> rows){
        // Use ArrayList for speed
        List<List<Object>> outer = new ArrayList<>(rows.size());
        for(CSVRow row : rows){
            List<Object> inner = new ArrayList<>(row.size());
            inner.addAll(rows);
            outer.add(inner);
        }

        return outer;
    }

    /**
     * Removes the last command done and reverts the changes. This does not add to the stack of commands, it undo and
     * the edit is lost forever. If there isn't an edit done, nothing happens.
     */
    public void undo(){
        Command lastEdit = commands.poll();
        // If there isn't a command to undo, don't do anything
        if(lastEdit == null){
            return;
        }
        update(lastEdit.getColumn(), lastEdit.getRow(), lastEdit.getOldValue());
    }

    /**
     * Updates the view, but sets the top left cell to be a certain cell.
     * @param column Column of the cell at the top left.
     * @param row Row of the cell at the top left.
     */
    public void show(int column, int row){
        head.updateScreen(column, row);
    }

    public CommandBuilder getCommandBuilder(){
        return this.commandBuilder;
    }
}
