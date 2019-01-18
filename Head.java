import com.google.api.client.auth.oauth2.Credential;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;
    private final Screen screen;
    private final boolean isConnectedToGoogle;
    private final CSVAccess csvAccess;
    private final AtomicBoolean isProgramRunning = new AtomicBoolean(true);

    public Head(Path pathToCSV){
        this.csvAccess = new CSVAccess(pathToCSV);
        this.isConnectedToGoogle = false;
        this.csvRepresentation = new CSVRepresentation(csvAccess, this);
        screen = new Screen(csvRepresentation, isProgramRunning);

        // Start threads
        screen.start();
    }

    public Head(Credential credential, String sheetID){
        this.csvAccess = new CSVAccess(credential, sheetID);
        this.isConnectedToGoogle = true;
        this.csvRepresentation = new CSVRepresentation(csvAccess, this);
        screen = new Screen(csvRepresentation, isProgramRunning);

        // start threads
        screen.start();
        googleUpdaterThread();
    }

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