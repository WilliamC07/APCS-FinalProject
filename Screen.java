import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import static com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;

import java.util.concurrent.atomic.AtomicInteger;

public class Screen extends Thread {
    private final CSVRepresentation csvRepresentation;
    private volatile boolean requestScreenUpdate = false;
    /**
     * Initializes value of one. This is the top left cell of the screen. Atomic Integer to ensure that
     * there is no thread mess up.
     */
    private volatile AtomicInteger startRow = new AtomicInteger(), startColumn = new AtomicInteger();
    private final CommandBuilder commandBuilder;

    /**
     * Constructor
     *
     * @param csvRepresentation The CSV to be shown to the screen
     */
    public Screen(CSVRepresentation csvRepresentation) {
        this.csvRepresentation = csvRepresentation;
        this.commandBuilder = csvRepresentation.getCommandBuilder();
    }

    /**
     * This updates the screen and listens to the user keystrokes.
     */
    @Override
    public void run() {
        Terminal terminal = TerminalFacade.createTextTerminal();
        com.googlecode.lanterna.screen.Screen screen = new com.googlecode.lanterna.screen.Screen(terminal);
        // Enter private mode and start the showing for the program
        screen.startScreen();

        // Show the file to the user
        updateScreen(screen);
        boolean isRunning = true;
        while (isRunning) {
            // Process keystroke
            Key key = terminal.readInput();
            // If there is a keystroke, then update the screen and handle the keypress
            if (key != null) {
                switch (key.getKind()) {
                    case Escape:
                        screen.stopScreen();
                        isRunning = false; // stop the thread
                        csvRepresentation.save();
                        break;
                    case ArrowUp:
                        // Can't make another row by going upwards
                        startRow.addAndGet(startRow.get() == 0 ? 0 : -1);
                        break;
                    case ArrowDown:
                        // Can make an **infinite** amount of cells downwards
                        startRow.addAndGet(1);
                    case ArrowLeft:
                        // Cannot make infinite amount of cells to the left
                        startColumn.addAndGet(startColumn.get() == 0 ? 0 : -1);
                        break;
                    case ArrowRight:
                        // Can make an **infinite** amount of cells to the right
                        startColumn.addAndGet(1);
                        break;
                    case NormalKey:
                    case Backspace:
                    case Enter:
                        // Make the command
                        commandBuilder.addChar(key);
                        break;
                }

                // update the screen with the latest content
                updateScreen(screen);
            }

            // update the screen if it has been requested by another part of the program
            if (requestScreenUpdate) {
                updateScreen(screen);
                requestScreenUpdate = false; // only update it once per request
            }
        }
    }

    /**
     * Forces the terminal to update with the latest information.
     */
    public void forceScreenUpdate(){
        requestScreenUpdate = true;
    }

    /**
     * Shows the new content onto the screen. This will clear the screen and move the cursor.
     *
     * @param terminal Terminal in which to output the characters
     */
    private void updateScreen(com.googlecode.lanterna.screen.Screen screen) {
        screen.clear();
        screen.setCursorPosition(0, 0);

        TerminalSize size = screen.getTerminalSize();
        // reserve one row for the user input
        String[] csvGrid = getTable(size.getColumns(), size.getRows() - 1, 10);
        for(int row = 0; row < size.getRows() - 1; row++){
            screen.putString(0, row, csvGrid[row], null, null);
        }

        // Subtract one so it goes to the bottom of the screen
        screen.putString(0, size.getRows() - 1, commandBuilder.toString(), null, null);

        // Prevents flickering for larger screens when terminal is enlarged
        screen.completeRefresh();
    }

    /**
     * A cell can only fit a certain amount of text, so we have to cut off parts that do not fit
     *
     * @param value   The string you are attempting to add
     * @param spacing The amount of space you have in the cell
     * @return A new string that can fit in the cell
     */
    public String fitSpace(String value, int spacing) {
        if (spacing < value.length()) {
            return value.substring(0, spacing);
        }

        // Adds space if the string is too short
        StringBuilder paddedString = new StringBuilder(value);
        while(paddedString.length() < spacing){
            paddedString.append(" ");
        }
        return paddedString.toString();
    }

    /**
     * Creates a table given the inputs
     *
     * @param columns     The number of columns in the table
     * @param rows        The number of rows in the table
     * @param cellspacing The size of the cells
     * @return An empty table with the inputs
     */
    public String[] getTable(int columns, int rows, int cellspacing) {
        String[] gird = new String[rows];
        for (int row = 0; row < rows; row++) {
            // Divide by two since every other line shows information
            int csvRow = startRow.get() + row / 2;
            // Every other row has a dashed line (starting with row 1)
            if(row % 2 == 0){
                gird[row] = repeat("-", columns);
            }else{
                // All other lines are able to fit data and a divider
                StringBuilder rowBuilder = new StringBuilder();
                for (int column = 0; column < columns;) {
                    int csvColumn = startColumn.get() + column / (cellspacing + 1);
                    // Check if we can fit a cell, add one because we need to fit a divider
                    if(columns - column + 1 < cellspacing){
                        // Cannot fit another column, so just use up remaining space
                        // Don't add a divider since we want to fit as much data as we can
                        int repeatAmount = columns - column;
                        rowBuilder.append(fitSpace(csvRepresentation.getValue(csvColumn, csvRow), repeatAmount));
                        column += repeatAmount;
                    }else{
                        // Can fit another column
                        rowBuilder.append(fitSpace(csvRepresentation.getValue(csvColumn, csvRow), cellspacing) + "|");
                        column += cellspacing + 1; // add one for the divider
                    }
                }
                gird[row] = rowBuilder.toString();
            }
        }
        return gird;
    }

    /**
     * Repeats a character a certain number of times.
     * @param string String to be repeated
     * @param times Number of times to repeat the string
     * @return String that contains the given value repeated by the given value.
     */
    private String repeat(String string, int times){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < times; i++){
            builder.append(string);
        }
        return builder.toString();
    }
}
