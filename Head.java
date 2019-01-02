import java.nio.file.Path;

/**
 * Head of the program.
 */
public class Head{
    private final CSVRepresentation csvRepresentation;

    public Head(Path pathToCSV){
        this.csvRepresentation = new CSVRepresentation(pathToCSV);
    }
}