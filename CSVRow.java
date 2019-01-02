/**
 * Each object of this class represents each row of the CSV file.
 */
public class CSVRow {


    /**
     * A node represents a cell in the csv file. This is what is between the comma(s)
     */
    private class CSVNode{
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
         * Creates a cell in the row.
         * @param data Data that will be stored in the cell.
         * @param previous Reference to the previous node
         * @param next Reference to the
         */
        CSVNode(String data, CSVNode previous, CSVNode next){
            this.data = data;
            this.previous = previous;
            this.next = next;
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
