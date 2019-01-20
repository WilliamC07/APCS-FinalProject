import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Screen extends Thread {

    /**
     * This is how we keep track of whether user wants a header or not
     */
    private boolean header;
    /**
     * This is how the screen gain access to the CSV and get the content to display on the screen.
     */
    private final CSVRepresentation csvRepresentation;
    /**
     * The screen refreshes every certain amount of milliseconds. The time is determined at {@link #pauseTime}.
     */
    private volatile boolean requestScreenUpdate = false;
    /**
     * This is how long it takes for the screen to check if anything needs to be shown to the screen and read key strokes.
     */
    private final int pauseTime = 5;
    /**
     * Initializes value of one. This is the top left cell of the screen. Atomic Integer to ensure that
     * there is no thread mess up.
     */
    private volatile AtomicInteger startRow = new AtomicInteger(), startColumn = new AtomicInteger();
    /**
     * This is how commands the user gives will be displayed to the screen. When the user presses the enter key, the
     * command will be processed and the screen will be requested to be updated.
     */
    private final CommandBuilder commandBuilder;
    /**
     * If true, the program is active and things need to be drawn to the screen.
     */
    private final AtomicBoolean isProgramRunning;
    /**
     * The amount of characters that can fit in one column. There is an initial value of 10 character per column.
     */
    private volatile int columnSize = 10;

    public boolean getHeader(){
      return header;
    }

    public void setHeader(boolean newHeader){
      header = newHeader;
    }

    /**
     * Constructor. Only one of these should be created.
     * @param csvRepresentation The CSV to be shown to the screen
     */
    public Screen(CSVRepresentation csvRepresentation, AtomicBoolean isProgramRunning) {
        this.csvRepresentation = csvRepresentation;
        this.commandBuilder = csvRepresentation.getCommandBuilder();
        this.isProgramRunning = isProgramRunning;
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
        while (isProgramRunning.get()) {
            // If the screen resized, we need to redraw the board
            if(screen.updateScreenSize()){
                updateScreen(screen);
            }

            // Process keystroke
            Key key = terminal.readInput();
            // If there is a keystroke, then update the screen and handle the keypress
            if (key != null) {
                switch (key.getKind()) {
                    case Escape:
                        screen.stopScreen();
                        isProgramRunning.set(false); // stop the thread
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

            // Prevent java from eating the cpu since this is on an infinite loop.
            try{
                Thread.sleep(pauseTime);
            }catch(InterruptedException e){
                // Do nothing, there is nothing to save or manage
            }
        }
    }

    /**
     * Forces the terminal to update with the latest information with a starting point (starting cell at the top left).
     * @param column Column of the csv file for the top left corner. Negative values ignored
     * @param row Row of the csv file for the top left corner. Negative values ignored
     */
    public void forceScreenUpdate(int column, int row){
        // Do nothing if there are negative values
        if(column < 0 || row < 0){
            return;
        }
        startColumn.set(column);
        startRow.set(row);
        requestScreenUpdate = true;
    }

    /**
     * Forces the terminal to update with the latest information.
     */
    public void forceScreenUpdate(){
        requestScreenUpdate = true;
    }

    /**
     * Shows the new content onto the screen. This will clear the screen and move the cursor.
     * @param screen Screen in which to output the characters
     */
    private void updateScreen(com.googlecode.lanterna.screen.Screen screen) {
        screen.clear();
        screen.setCursorPosition(0, 0);

        TerminalSize size = screen.getTerminalSize();
        // reserve one row for the user input
        String[] csvGrid = getTable(size.getColumns(), size.getRows() - 1);
        for(int row = 0; row < size.getRows() - 1; row++){
            screen.putString(0, row, csvGrid[row], null, null);
        }

        String userInput = commandBuilder.toString();
        // Cut off the beginning part if the user is typing a string longer than the width of the terminal. Minus one
        // because the cursor takes up one column as well.
        if(userInput.length() > size.getColumns() - 1){
            // Add one so we can fit the cursor
            userInput = userInput.substring(userInput.length() - size.getColumns() + 1);
        }
        screen.putString(0, size.getRows() - 1, userInput, null, null);

        // Move the cursor to the end of what the user is typing
        screen.setCursorPosition(userInput.length(), size.getRows() - 1);

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
    private String fitSpace(String value, int spacing) {
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
     * @return An empty table with the inputs
     */
    private String[] getTable(int columns, int rows) {
        String[] gird = new String[rows];
        for (int row = 0; row < rows; row++) {
            // Divide by two since every other line shows information
            // Minus one because the first row is reserved for displaying the column number
            int csvRow = startRow.get() + row / 2 - 1;
            // Every other row has a dashed line (starting with row 1)

            if(row % 2 == 0){
                grid[row] = repeat("-", columns);
            }else{
                // All other lines are able to fit data and a divider
                StringBuilder rowBuilder = new StringBuilder();
                // Leave the incrementation to when we print things to the screen because it is easier to understand
                // that way
                for (int column = 0; column < columns;) {
                  if (getHeader()){
                    //Have to subtract 2 because the first column is reserved for displaying the row number and there is a header
                    int csvColumn = startColumn.get() + column / (cellSpacing + 1) - 2;
                  }
                  else{
                    //Have to subtract 1 because the first column is reserved for displaying the row number and there is no header
                    int csvColumn = startColumn.get() + column / (cellSpacing + 1) - 1;
                  }
                    String valueToDisplay;
                    // Note: In these conditionals, we are using row and column (that is the position in the terminal,
                    // not the position in the csv because labeling row and column is always the top or left of the
                    // terminal
                    if(column == 0 && row == 1){
                        // Do not show anything for the top left cell because there isn't a label there
                        // Zeroth column represents the first cell on the left hand side of the screen
                        // First row (index 1 not 0) is because the zeroth (index 0) row is always a dashed line, not a cell
                        valueToDisplay = fitSpace("", columnSize);
                    }else if(column == 0){
                        // The left most column is used to display what row the user is looking at (row number)
                        valueToDisplay = fitSpace(String.valueOf(csvRow), columnSize);
                    }else if(row == 1){
                        // The 0th row is always a dashed line, so the text can begin showing in the 1st row
                        // The top row is used to display what row the user is looking at (column number)
                        valueToDisplay = fitSpace(String.valueOf(csvColumn), columnSize);
                    }else{
                        // All other columns are used to display content
                        valueToDisplay = fitSpace(csvRepresentation.getValue(csvColumn, csvRow), columnSize);
                    }

                    // Check if we can fit a cell, add one because we need to fit a divider
                    if(columns - column + 1 < columnSize){
                        // Cannot fit another column, so just use up remaining space
                        // Don't add a divider since we want to fit as much data as we can
                        int spaceLeft = columns - column;
                        rowBuilder.append(fitSpace(valueToDisplay, spaceLeft));
                        column += spaceLeft;
                    }else{
                        // Can fit another column
                        rowBuilder.append(valueToDisplay).append("|");
                        column += columnSize + 1; // add one for the divider
                    }
                }
                grid[row] = rowBuilder.toString();
            }
        }
        return grid;
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

    /**
     * Sets the size of the column. This will also refresh the screen to show the change.
     * @param newSize Amount of characters that can fit in a column
     */
    public void setColumnSize(int newSize){
        this.columnSize = newSize;
        forceScreenUpdate();
    }
}
