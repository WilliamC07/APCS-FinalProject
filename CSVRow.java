import java.util.LinkedList;

/**
 * Each object of this class represents each row of the CSV file.
 */
public class CSVRow extends LinkedList<CSVNode>{
    public static int largestRowSize = 0;

    private CSVRow(){

    }

    public static CSVRow createNewRow(String rowStringRepresentation){
        CSVRow row = new CSVRow();
        char quote = '"';
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
                    if(cellStringRepresentation == null){
                        // This mean it is the start of a new cell
                        cellStringRepresentation = new StringBuilder();
                    }else{
                        // This means it is the end of the cell
                        row.add(CSVNode.of(cellStringRepresentation.toString()));
                        // Reset it it to denote that we
                        cellStringRepresentation = null;
                    }
                }
            }
        }

        return row;
    }

    /**
     * Converts the given row into a string. If the row isn't long enough, it will add extra commas.
     * @return String representation of this row
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        char quote = '"';
        int amountOfCellsAdded = 0;
        for(CSVNode node : this){
            builder.append(quote).append(node.toString()).append(quote);
            amountOfCellsAdded++;
        }

        // Need to add empty cells to make the csv complete by adding commas
        while(amountOfCellsAdded != largestRowSize){
            builder.append(quote);
            amountOfCellsAdded++;
        }

        return builder.toString();
    }
}
