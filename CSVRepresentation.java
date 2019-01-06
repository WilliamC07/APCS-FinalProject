import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

public class CSVRepresentation {
    private final Path pathToCSV;
    private final CSVAccess csvAccess;
    private final LinkedList<CSVRow> rows;

    public CSVRepresentation(Path pathToCSV){
        this.pathToCSV = pathToCSV;
        this.csvAccess = new CSVAccess(pathToCSV);
        this.rows = readCSV();
    }

    public synchronized String getValue(int column, int row){
        try{
            return rows.get(row).get(column).toString();
        }catch(IndexOutOfBoundsException e){
            // This means the cell doesn't exists, so we can just return an empty string
            return "";
        }
    }

    /**
     * Reads the CSV file given by the user and creates a LinkedList from it.
     * @return A LinkedList that contains a row of cells of the CSV file.
     */
    private LinkedList<CSVRow> readCSV(){
        LinkedList<CSVRow> rows = new LinkedList<>();
        Scanner fileReader = csvAccess.readCSV();
        while(fileReader.hasNextLine()){
            rows.add(CSVRow.createNewRowFromString(fileReader.nextLine()));
        }
        return rows;
    }

}
