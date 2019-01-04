import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Scanner;

public class CSVAccess {
    private final Path pathToCSV;

    public CSVAccess(Path pathToCSV){
        this.pathToCSV = pathToCSV;
    }

    public Scanner readCSV(){
        try{
            return new Scanner(pathToCSV);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1); // Cannot read file, so stop
        }

        // This will never be called
        return null;
    }

    /**
     * Converts each row to a string and saves it onto the file given at the start of the program.
     * @param rows All the rows to be saved
     */
    public void saveCSV(LinkedList<CSVRow> rows){
        try(FileWriter fileWriter = new FileWriter(pathToCSV.toFile());
            PrintWriter printWriter = new PrintWriter(fileWriter)){
            for(CSVRow row : rows){
                printWriter.println(row.toString());
            }
        }catch(IOException e){
            // Could not save the file
            System.out.println("Fail to save");
            e.printStackTrace();
        }
    }
}
