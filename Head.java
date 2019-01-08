import java.nio.file.Path;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;
    private final Screen screen;

    public Head(Path pathToCSV){
        this.csvRepresentation = new CSVRepresentation(pathToCSV, this);
        screen = new Screen(csvRepresentation);

        // Start threads
        screen.start();
    }

    /**
     * Forces the terminal screen to update and show the newest changes.
     */
    public void updateScreen(){
        screen.forceScreenUpdate();
    }
}