import com.google.api.client.auth.oauth2.Credential;

import java.nio.file.Path;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;
    private final Screen screen;
    private final boolean isConnectedToGoogle;

    public Head(Path pathToCSV){
        this.isConnectedToGoogle = false;
        this.csvRepresentation = new CSVRepresentation(pathToCSV, this);
        screen = new Screen(csvRepresentation);

        // Start threads
        screen.start();
    }

    public Head(Credential credential, String sheetID){
        this.isConnectedToGoogle = true;
        this.csvRepresentation = new CSVRepresentation(credential, sheetID, this);
        screen = new Screen(csvRepresentation);

        // start threads
        screen.start();
    }

    /**
     * Forces the terminal screen to update and show the newest changes.
     */
    public void updateScreen(){
        screen.forceScreenUpdate();
    }

    /**
     * Forces the terminal screen to update to show the latest changes with the top left corner set as a certain cell.
     * @param column Column of the cell to show at the top left
     * @param row Row of the cell to show at the top left
     */
    public void updateScreen(int column, int row){
        screen.forceScreenUpdate(column, row);
    }

    public boolean isConnectedToGoogle() {
        return isConnectedToGoogle;
    }
}