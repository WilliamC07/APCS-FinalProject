import java.nio.file.Path;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;
    private final Screen screen;

    public Head(Path pathToCSV){
        this.csvRepresentation = new CSVRepresentation(pathToCSV);
        screen = new Screen(csvRepresentation);

        // Start threads
        screen.start();
    }
}