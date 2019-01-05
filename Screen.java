import com.googlecode.lanterna.TerminalFacade;
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

        while(isRunning){

        }
    }
}
