import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
            BufferedWriter writer = new BufferedWriter(fileWriter)){
            int largestRowSize = simplifyRows(rows);
            // This contains everything that needs to be saved to disk.
            // We are using it for Last In First Out (LIFO) since we are reading the rows backwards
            LinkedList<String> parts = new LinkedList<>();

            // Loop through backwards so we can remove useless lines, those are lines that only contain comma(s) and
            // are not between lines that contain characters other than comma(s).
            // If this is true, that means all lines need to be kept
            boolean keepMove = false;
            for(int i = rows.size() - 1; i >= 0; i--){
                String representation = rows.get(i).getFileRepresentation(largestRowSize);
                // Only add an additional line if there is something to add (not only commas)
                // The row is most likely contains the cell that was deleted
                // We do have to keep an empty line if it is between two lines that are not empty
                if(keepMove || !isEmptyRow(representation)){
                    // Each row ends with a end of line delimiter
                    parts.addFirst(representation);
                    // All other lines need to be kept (so there isn't a gap)
                    keepMove = true;
                }
            }

            // Go through the stack to write to disk
            String part;
            while((part = parts.pollFirst()) != null){
                writer.write(part);
                writer.newLine();
            }
        }catch(IOException e){
            // Could not save the file
            System.out.println("Fail to save");
            e.printStackTrace();
        }
    }

    /**
     * Checks if the row is empty, that is if the string representation of the row (what will be written to disk) only
     * contains comma(s). Commas are the delimiter for cells.
     * @param representation The string representation of the cell that will be written to disk. No "\n" allow.
     * @return True if the row only contains comma(s); false if the row contains other characters
     */
    private boolean isEmptyRow(String representation){
        for(int i = 0; i < representation.length(); i++){
            if(representation.charAt(i) != ','){
                return false;
            }
        }
        // If the row only contains commas, it is technically empty
        return true;
    }

    /**
     * Removes all ending the cells that are empty located at the end of each row.
     * @param rows List of rows
     * @return The greatest number of columns
     */
    private int simplifyRows(LinkedList<CSVRow> rows){
        int largest = 0;
        for(CSVRow row : rows){
            // removes excess cells
            row.simplify();
            // Keep track of the row size
            if(row.size() > largest){
                largest = row.size();
            }
        }
        return largest;
    }
}
