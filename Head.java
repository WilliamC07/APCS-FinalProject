import com.google.api.client.auth.oauth2.Credential;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;
    private final Screen screen;
    /**
     * True if the user is connected to Google API, false otherwise.
     */
    private final boolean isConnectedToGoogle;
    private final CSVAccess csvAccess;
    /**
     * Determines if all the threads should quit so the program will end.
     */
    private final AtomicBoolean isProgramRunning = new AtomicBoolean(true);

    /**
     * Constructor for when the user is editing a csv file on disk.
     * @param pathToCSV Path to the csv file on disk.
     */
    public Head(Path pathToCSV){
        this.csvAccess = new CSVAccess(pathToCSV);
        this.isConnectedToGoogle = false;
        this.csvRepresentation = new CSVRepresentation(csvAccess, this);
        screen = new Screen(csvRepresentation, isProgramRunning);

        // Start threads
        screen.start();
    }

    /**
     * Constructor for when the user is attempting to connect to the Google API.
     * @param credential Credentials created from the file given
     * @param sheetID SpreadSheetID of the Google Sheet to be edited
     */
    public Head(Credential credential, String sheetID){
        this.csvAccess = new CSVAccess(credential, sheetID);
        this.isConnectedToGoogle = true;
        this.csvRepresentation = new CSVRepresentation(csvAccess, this);
        screen = new Screen(csvRepresentation, isProgramRunning);

        // start threads
        screen.start();
        googleUpdaterThread();
    }

    /**
     * Spawns a thread and run it to update this program's csv with the one on Google. Checks for update every second
     * to prevent the screen from updating too often and flickering and because the Google API only allows around 500
     * requests every 100 seconds.
     */
    private void googleUpdaterThread(){
        Thread updateCSVFromGoogle = new Thread(() -> {
            while(isProgramRunning.get()){
                // recreate the csv file
                csvRepresentation.updateCSV(csvAccess.readCSV());
                // show the new csv file
                screen.forceScreenUpdate();

                // Sleep every second to prevent flickering screen
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    // Do nothing
                }
            }
        });

        updateCSVFromGoogle.start();
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

    /**
     * Updates the screen to have the amount of characters that can fit in a column to the given value. This will
     * also refresh the screen.
     * @param newSize The amount of characters that can fit in one column.
     */
    public void resizeColumn(int newSize){
        screen.setColumnSize(newSize);
    }

    public boolean isConnectedToGoogle() {
        return isConnectedToGoogle;
    }
}