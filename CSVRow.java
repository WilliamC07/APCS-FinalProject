import java.util.Iterator;
import java.util.LinkedList;

/**
 * Each object of this class represents each row of the CSV file.
 */
public class CSVRow extends LinkedList<CSVNode>{
    private CSVRow(){

    }

    @Override
    public CSVNode set(int index, CSVNode n){
        // Add additional cells
        while(index >= size()){
            // Blank cell
            add(CSVNode.newInstance(""));
        }

        return super.set(index, n);
    }

    @Override
    public CSVNode remove(int index){
        // Do nothing if it goes out of bounds
        if(index < 0 || index >= size()){
            return null;
        }
        return super.remove(index);
    }

    /**
     * Creates a new row given the string representation of the row. A comma is used to separate cells.
     * @param rowStringRepresentation The string representation of the row
     * @return A new instance of this class given the following information
     */
    public static CSVRow createNewRowFromString(String rowStringRepresentation){
        CSVRow row = new CSVRow();
        char quote = '"';
        boolean cellStartsWithQuote = false;
        StringBuilder cellStringRepresentation = null;

        for(int i = 0; i < rowStringRepresentation.length(); i++){
            char c = rowStringRepresentation.charAt(i);

            if(c == quote){
                if(i != rowStringRepresentation.length() - 1 && rowStringRepresentation.charAt(i + 1) == quote){
                    // Double double quotes gets passed in
                    cellStringRepresentation.append(quote).append(quote);
                    // Skip the next double quote since we already appended it
                    i++;
                }else{
                    // Single double quote is ignored and means it is the start/end of a cell
                    // Have to check length too because a quote may start a line in the csv file
                    if(cellStringRepresentation == null || cellStringRepresentation.length() == 0){
                        // This mean it is the start the first row
                        // So the csv string might look like --> "asd,",banana
                        cellStringRepresentation = new StringBuilder();
                        cellStartsWithQuote = true;
                    }else{
                        // This means it is the end of the cell
                        // The following comma after the cell will add it to the row or if this is the last cell,
                        // it will still be added by the external add outside this loop

                        // We don't know if the next cell starts with quote
                        cellStartsWithQuote = false;
                    }
                }
            }else if(c == ','){
                // Only add commas if they are part of a cell (if the cell is in quotes, the comma is considered part of it
                if(cellStringRepresentation != null && cellStartsWithQuote){
                    cellStringRepresentation.append(',');
                }else{
                    // Comma means the end of a cell, so add that and make a new builder
                    if(cellStringRepresentation == null){
                        cellStringRepresentation = new StringBuilder();
                    }
                    // Multiple commas in a row can be used to represent empty cells
                    row.add(CSVNode.of(cellStringRepresentation.toString()));
                    cellStringRepresentation = new StringBuilder();
                }
            }else{
                // All other characters are valid and nothing needs to be done
                // Make an instance of the StringBuilder if there isn't one (happens for the first cell of each row
                if(cellStringRepresentation == null){
                    cellStringRepresentation = new StringBuilder();
                }
                cellStringRepresentation.append(c);
            }
        }

        // Add the last cell to the csv if there is a cell to add
        if(cellStringRepresentation != null){
            row.add(CSVNode.of(cellStringRepresentation.toString()));
        }

        return row;
    }

    public static CSVRow createEmptyRow(){
        return new CSVRow();
    }

    /**
     * Removes all cells at the end of the list that does not contain data (empty string)
     */
    public void simplify(){
        CSVNode last;
        while((last = peekLast()) != null && last.getData().equals("")){
            pollLast();
        }
    }

    /**
     * Converts the given row into a string. If the row isn't long enough, it will add extra commas.
     * @return String representation of this row
     */
    public String getFileRepresentation(int largestRowSize){
        StringBuilder builder = new StringBuilder();
        char comma = ',';
        int amountOfCellsAdded = 0;  // Keep track of amount of cells so all rows have equal amount of columns
        for(int i = 0; i < size(); i++){
            CSVNode node = get(i);
            builder.append(node.toString());

            // add commas only if it is not the last cell of the row
            if(i < size() - 1){
                builder.append(comma);
            }

            amountOfCellsAdded++;
        }
        // Need to add empty cells to make the csv complete by adding commas
        while(amountOfCellsAdded != largestRowSize){
            builder.append(comma);
            amountOfCellsAdded++;
        }

        return builder.toString();
    }
}
