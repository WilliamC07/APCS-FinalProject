import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

import java.util.ArrayDeque;

public class Screen extends Thread {
    private final CSVRepresentation csvRepresentation;
    private volatile boolean requestScreenUpdate = false;

    /**
     * Constructor
     *
     * @param csvRepresentation The CSV to be shown to the screen
     */
    public Screen(CSVRepresentation csvRepresentation) {
        this.csvRepresentation = csvRepresentation;
    }

    /**
     * This updates the screen and listens to the user keystrokes.
     */
    @Override
    public void run() {
        Terminal terminal = TerminalFacade.createTextTerminal();
        boolean isRunning = true;
        terminal.enterPrivateMode();
        // Prevents the user from writing text to the screen directly, text only goes in the last line
        terminal.setCursorVisible(false);
        // Show the table onto the screen
        updateScreen(terminal);
        TerminalSize terminalSize = terminal.getTerminalSize();

        while (isRunning) {
            // Refresh the view if the terminal has been resized
            if(!terminal.getTerminalSize().equals(terminalSize)){
                updateScreen(terminal);
            }

            // Process the keystrokes if there are any
            Key key = terminal.readInput();
            if (key != null) {
                // Only update the view if there is something to update
                updateScreen(terminal);
            }

            // Update the screen if a function requests it
            if(requestScreenUpdate){
                updateScreen(terminal);
                requestScreenUpdate = false; // only update it once per request
            }

            // Sleep so the program doesn't take up the entire cpu
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing
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
    private void updateScreen(Terminal terminal) {
        terminal.clearScreen();

        // Move cursor to start since it doesn't get reset by clearing the screen
        terminal.moveCursor(0, 0);

        // Add the new content (or old content if nothing changed)
        String table = getTable(terminal.getTerminalSize().getColumns(), terminal.getTerminalSize().getRows(), 10);
        for (int i = 0; i < table.length(); i++) {
            terminal.putCharacter(table.charAt(i));
        }

        // Prevents flickering for larger screens when terminal is enlarged
        terminal.flush();
    }

    /**
     * A cell can only fit a certain amount of text, so we have to cut off parts that do not fit
     *
     * @param value   The string you are attempting to add
     * @param spacing The amount of space you have in the cell
     * @return A new string that can fit in the cell
     */
    public String fitSpace(String value, int spacing) {
        if (spacing > value.length()) {
            return value;
        }
        int i = 0;
        String s = "";
        while (i < spacing) {
            s += value.charAt(i);
            i++;
        }
        return s;
    }

    /**
     * Creates a table given the inputs
     *
     * @param columns     The number of columns in the table
     * @param rows        The number of rows in the table
     * @param cellspacing The size of the cells
     * @return An empty table with the inputs
     */
    public String getTable(int columns, int rows, int cellspacing) {
        String s = "";
        for (int j = 0; j < rows; j++) {
            for (int x = 0; x < columns; x++) {
                int i = 0;
                if (i < cellspacing && j % 2 == 1) {
                    s += "-";
                    i++;
                }
                if (x % cellspacing == 0 && x != 0 && j % 2 == 0) {
                    s += "|";
                } else if (j % 2 == 0 && x % cellspacing != 0) {
                    s += " ";
                }
            }
            s += "\n";
        }
        return s;
    }
}
