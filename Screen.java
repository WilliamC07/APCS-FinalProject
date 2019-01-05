import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.util.ArrayDeque;

public class Screen extends Thread{
    private final CSVRepresentation csvRepresentation;

    /**
     * Constructor
     * @param csvRepresentation The CSV to be shown to the screen
     */
    public Screen(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    /**
     * This updates the screen and listens to the user keystrokes.
     */
    @Override
    public void run(){
        Terminal terminal = TerminalFacade.createTextTerminal();
        boolean isRunning = true;
        terminal.enterPrivateMode();
        // Prevents the user from writing text to the screen directly, text only goes in the last line
        terminal.setCursorVisible(false);

        while(isRunning){
            // Removes previous content
            terminal.clearScreen();

            // Show the table onto the screen
            updateScreen(terminal);

            // Process the keystrokes if there are any
            Key key = terminal.readInput();
            if(key != null){

            }

            // Sleep so the program doesn't take up the entire cpu
            try{
                Thread.sleep(8);
            }catch(InterruptedException e){
                // Do nothing
            }
        }
    }

    /**
     * Shows the new content onto the screen. This assumes that the screen has been cleared already and is in private
     * mode.
     * @param terminal Terminal in which to output the characters
     */
    private void updateScreen(Terminal terminal){
        // Add the new content (or old content if nothing changed)
        String table = "Sloths are cool"; // TODO update to use samson's method
        for(int i = 0; i < table.length(); i++){
            terminal.putCharacter(table.charAt(i));
        }
    }
}
