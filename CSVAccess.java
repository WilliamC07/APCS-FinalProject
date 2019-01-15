import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;
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
     * Gets the entire file from the given google sheet.
     *
     * TODO: implement credentials
     * @param spreadSheetID ID for the google sheet. Found in https://docs.google.com/spreadsheets/d/spreadsheetId/edit
     * @return List of a list of objects representing the sheet.
     * @throws GeneralSecurityException From creating a secure connection to google API
     * @throws IOException From creating a secure connection to google API
     */
    public List<List<Object>> getGoogleSheets(String spreadSheetID) throws GeneralSecurityException, IOException{
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        String spreadsheetId = spreadSheetID;
        String range = "Sheet1"; // gets everything
        JsonFactory factory = JacksonFactory.getDefaultInstance();

        // TODO: Make httpRequestInitializer
        Sheets service = new Sheets.Builder(httpTransport, factory, null).build();
        ValueRange values = service.spreadsheets().values().get(spreadsheetId, range).execute();
        List<List<Object>> content = values.getValues();
        for(List<Object> row : content){
            for(Object cell : row){
                System.out.println(cell);
            }
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
