import com.google.api.client.auth.oauth2.Credential;
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
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVAccess {
    /**
     * This is used when the user is opening an existing csv file on disk.
     */
    private Path pathToCSV = null;
    /**
     * This is used when the user is connected to Google server. This is how the user will be able to connect, the
     * credentials.
     */
    private Credential credential;
    /**
     * This is the ID of the google sheet. It can be found at https://docs.google.com/spreadsheets/d/spreadsheetId/edit.
     * Where spreadsheetID is the string of text in the url.
     */
    private String sheetID;

    /**
     * Spreadsheet from Google API. This is how you communicate with the server.
     */
    private Sheets sheet;

    /**
     * Creates an instance of this class that will read data from a file on the user's disk.
     * @param pathToCSV Path to the csv file
     */
    public CSVAccess(Path pathToCSV){
        this.pathToCSV = pathToCSV;
    }

    /**
     * Creates an instance of this class that will read data from Google Sheets. Data will be stored on the google
     * sheet.
     * @param credential Credentials to connect to the Google Sheet.
     */
    public CSVAccess(Credential credential, String sheetID){
        this.credential = credential;
        // TODO: Replace with sheetID
        this.sheetID = sheetID;
        this.sheet = connectToGoogle();
    }

    /**
     * Reads the csv file and returns a list of a list of rows for the user.
     * @return The list of a list representation of the csv.
     */
    public LinkedList<CSVRow> readCSV(){
        if(pathToCSV == null){
            // Means we are connected to Google
            return csvFromGoogle();
        }else{
            return csvFromDisk();
        }
    }

    /**
     * Converts each row to a string and saves it onto the file given at the start of the program. This will not save
     * anything to the Google Sheets. Each command done by the user should automatically be updated to Google Sheets.
     * @param rows All the rows to be saved
     */
    public void saveCSV(LinkedList<CSVRow> rows){
        if(pathToCSV != null){
            saveToDisk(rows);
        }

        // Do not save to google, each command done by the user should automatically update the Google Sheet for the
        // user.
    }

    /**
     * Updates the google sheet to have the most recent change.
     * @param column Column of the cell to be changed. This will be converted to A1 format.
     * @param row Row of the cell to be changed. This will be converted to A1 format.
     * @param newValue Value of the cell to be set to.
     */
    public void updateToGoogle(int column, int row, String newValue){
        String range = getGoogleSheetsRange(column, row);

        // Google api is weird. You only give the value you want to change into the ValueRange. Then, give it the range
        // of the google sheet the value should be set to.
        ValueRange valueRange = new ValueRange();
        valueRange.setRange(range);
        // our program is a list of rows
        valueRange.setMajorDimension("ROWS");

        List<Object> lists = new ArrayList<>();
        lists.add(newValue);
        List<List<Object>> values = new ArrayList<>();
        values.add(lists);
        valueRange.setValues(values);

        try{
            sheet.spreadsheets().values().update(sheetID, range, valueRange).setValueInputOption("RAW").execute();
        }catch(IOException e){
            e.printStackTrace();
            Main.exitWithError("Connection cannot be made, connection closed.");
        }
    }

    /**
     * Converts the column and row to A1 representation. The A1 representation is done by Sheet1"column":"row". Row is a
     * regular number while column is a series of letters. Each letter corresponds to the place in the alphabet, but
     * A is 0 and Z is 25. Any number greater than 25 needs to have multiple letters, so 26 is AA and 27 is AB. Sheet1
     * is used because we are getting the whole sheet.
     * @return A1 Representation.
     */
    private static String getGoogleSheetsRange(int column, int row){
        StringBuilder builder = new StringBuilder("Sheet1!");
        final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder firstCellCord = new StringBuilder();

        // Attach the column, which is made up of multiple letters
        // The 25th (zero based) column is "Z" and the 26th column is "AA"
        final int columnLetters = column / 25 + 1;
        for(int i = 0; i < columnLetters; i++){
            firstCellCord.append(ALPHABET[column % 26]); // Get the letter that corresponds to the number

            // Decrease the column since we are making the next part of the A1 representation.
            // Zero based, so 'Z' is the 25th letter
            column -= 25;
        }

        // Attach the row
        // Add one because rows are index starting from 1 and not 0 like this program
        firstCellCord.append(row + 1);

        // Add the first cell cord
        builder.append(firstCellCord);

        return builder.toString();
    }

    /**
     * Gets the entire csv representation and saves it to disk. This is only used if the user is editing a file from
     * the disk.
     */
    private void saveToDisk(LinkedList<CSVRow> rows){
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
     * Gets the entire file from the given google sheet. This is only used if the user is editing a file from google api.
     *
     * @return List of a list of objects representing the sheet.
     */
    private LinkedList<CSVRow> csvFromGoogle() {
        try{
            String range = "Sheet1"; // gets everything
            List<List<Object>> values = sheet.spreadsheets().values().get(sheetID, range).execute().getValues();

            // Convert to LinkedList of CSVRow
            LinkedList<CSVRow> formatted = new LinkedList<>();
            values.forEach(row -> formatted.add(CSVRow.fromList(row)));
            return formatted;
        }catch(IOException e){
            Main.exitWithError("Cannot connect to Google API");
        }

        // Will never reach
        return null;
    }

    /**
     * Connects to the google api and gets the sheet.
     * @return The sheet used to communicate with google sheet
     */
    private Sheets connectToGoogle(){
        try{
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JsonFactory factory = JacksonFactory.getDefaultInstance();

            // TODO: Make httpRequestInitializer
            return new Sheets.Builder(httpTransport, factory, credential).build();
        }catch(GeneralSecurityException | IOException e){
            Main.exitWithError("Cannot connect to Google API");
        }

        // Will never reach, since the catch statement quits the program
        return null;
    }

    /**
     * Gets the csv from disk. This is only used if the user is editing a file from disk.
     * @return Representation of the csv file from Google Sheets
     */
    private LinkedList<CSVRow> csvFromDisk(){
        LinkedList<CSVRow> csvTable =  new LinkedList<>();

        try{
            Scanner scanner = new Scanner(pathToCSV);
            while(scanner.hasNextLine()){
                csvTable.add(CSVRow.createNewRowFromString(scanner.nextLine()));
            }
            return csvTable;
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1); // Cannot read file, so stop
        }

        // will never be called
        return null;
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
