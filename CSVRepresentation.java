import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Scanner;

public class CSVRepresentation {
    private final Path pathToCSV;
    private final CSVAccess csvAccess;
    private final LinkedList<CSVRow> rows;
    private final ArrayDeque<Command> commands = new ArrayDeque<>();
    private final HandleCommand handleCommand;
    private final CommandBuilder commandBuilder;
    private final Head head;

    public CSVRepresentation(Path pathToCSV, Head head){
        this.pathToCSV = pathToCSV;
        this.head = head;
        this.csvAccess = new CSVAccess(pathToCSV);
        this.rows = readCSV();
        this.handleCommand = new HandleCommand(this);
        this.commandBuilder = new CommandBuilder(handleCommand);
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
     * Reads the CSV file given by the user and creates a LinkedList from it.
     * @return A LinkedList that contains a row of cells of the CSV file.
     */
    private LinkedList<CSVRow> readCSV(){
        LinkedList<CSVRow> rows = new LinkedList<>();
        Scanner fileReader = csvAccess.readCSV();
        while(fileReader.hasNextLine()){
            rows.add(CSVRow.createNewRowFromString(fileReader.nextLine()));
        }
        return rows;
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
        int column = command.getColumn();
        int row = command.getRow();
        while(row >= rows.size()){
            rows.add(CSVRow.createEmptyRow());
        }
        rows.get(row).set(column, CSVNode.newInstance(command.getNewValue()));
        // Screen update
        head.updateScreen();
    }

    public CommandBuilder getCommandBuilder(){
        return this.commandBuilder;
    }
}
