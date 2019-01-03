/**
 * Each object of this class represents each row of the CSV file.
 */
public class CSVRow {


    /**
     * A node represents a cell in the csv file. This is what is between the comma(s)
     */
    private static class CSVNode{
        /**
         * The data stored in the node.
         */
        private String data;
        /**
         * Reference to the previous node (this is the previous cell). Null if this node is the first cell.
         */
        private CSVNode previous;
        /**
         * Reference to the next node (this is the next cell). Null if this node is the last cell.
         */
        private CSVNode next;

        /**
         * Creates a new cell in the row.
         * @param data Data that will be stored in the cell.
         * @param previous Reference to the previous node
         * @param next Reference to the
         */
        private CSVNode(String data, CSVNode previous, CSVNode next){
            this.data = data;
            this.previous = previous;
            this.next = next;
        }

        /**
         * Static Factory Method to get an instance of this class.
         * Creates a new cell given the data to be stored, previous and next node. 
         * @param data Data to be stored on the list.
         * @param previous Previous node; null if this is the first node
         * @param next Next node; null if this is the last node
         * @return A new node
         */
        public static CSVNode newInstance(String data, CSVNode previous, CSVNode next){
            return new CSVNode(data, previous, next);
        }

        /**
         * Static Factory Method to get an instance of this class
         * Creates a new cell given the string representation of the node.
         * @param stringRepresentation String representation of the node defined by {@link #toString()}
         * @param previous Previous node, null if this is the first node
         * @param next Next node, null if this is the last node
         * @return A new node from the given string representation
         */
        public static CSVNode of(String stringRepresentation, CSVNode previous, CSVNode next){
            return new CSVNode(stringRepresentationToData(stringRepresentation), previous, next);
        }

        /**
         * Converts the string representation of the node defined by {@link #toString()} to data.
         * @param stringRepresentation String representation of the cell
         * @return Data to be stored in the cell.
         */
        private static String stringRepresentationToData(String stringRepresentation){

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
                        returnString.insert(0, quoteCharacter).append(quoteCharacter);
                    default:
                        returnString.append(characterAtIndex);
                }
            }

            return returnString.toString();
        }
    }
}
