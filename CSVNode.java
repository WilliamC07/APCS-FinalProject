/**
 * A node represents a cell in the csv file. This is what is between the comma(s)
 */
public class CSVNode{
    /**
     * The data stored in the node.
     */
    private String data;

    /**
     * Creates a new cell in the row.
     * @param data Data that will be stored in the cell.
     */
    private CSVNode(String data){
        this.data = data;
    }

    /**
     * Static Factory Method to get an instance of this class.
     * Creates a new cell given the data to be stored.
     * @param data Data to be stored on the list.
     * @return A new node
     */
    public static CSVNode newInstance(String data){
        return new CSVNode(data);
    }

    /**
     * Static Factory Method to get an instance of this class
     * Creates a new cell given the string representation of the node.
     * @param stringRepresentation String representation of the node defined by {@link #toString()}
     * @return A new node from the given string representation
     */
    public static CSVNode of(String stringRepresentation){
        return new CSVNode(stringRepresentationToData(stringRepresentation));
    }

    /**
     * Converts the string representation of the node defined by {@link #toString()} to data.
     * @param stringRepresentation String representation of the cell
     * @return Data to be stored in the cell.
     */
    private static String stringRepresentationToData(String stringRepresentation){
        StringBuilder builder = new StringBuilder();
        final char quote = '\"';
        for(int i = 0; i < stringRepresentation.length(); i++){
            char c = stringRepresentation.charAt(i);
            if(c == quote){
                // This means there are two double quotes in a row, which converts to a single quote.
                // Do add the quote if it is just a single one
                if(i != stringRepresentation.length() -1 && stringRepresentation.charAt(i+1) == quote){
                    builder.append(quote);
                }
            }else{
                // All other characters can be added normally
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Get the data stored in this node. DO NOT call {@link #toString()} to get the data stored inside to show the user.
     * @return Data stored in the cell
     */
    public String getData(){
        return data;
    }

    /**
     * This does NOT include the commas to separate each cell. It is formatted in this way:
     * 1. If a cell contains a comma, then wrap the whole cell in “ (double quotes)
     *    w, → “w,”
     * 2. If a cell contains a double quote, then put another one immediately after it
     *    w” → w””
     * 3. If a cell contains both a comma and double quote, then follow rule 1 and 2
     *    w,”” → “w, ” ” ” ” ” (ignore the white space and this text)
     * @return The string representation of this cell without commas to separate cells
     */
    @Override
    public String toString(){
        // Use a StringBuilder because it is more efficient when appending characters in a loop
        StringBuilder returnString = new StringBuilder();
        char quoteCharacter = '"';

        for(int i = 0; i < data.length(); i++){
            char characterAtIndex = data.charAt(i);
            switch(characterAtIndex){
                case '\"':
                    // If the user types in a double quote, it becomes two double quotes
                    returnString.append(quoteCharacter).append(quoteCharacter);
                    break;
                case ',':
                    // If the user types in a comma, the whole cell needs to be wrapped
                    returnString.insert(0, quoteCharacter).append(",").append(quoteCharacter);
                    break;
                default:
                    returnString.append(characterAtIndex);
            }
        }

        return returnString.toString();
    }
}