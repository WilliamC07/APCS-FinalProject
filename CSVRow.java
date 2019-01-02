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
         * 3. If a cell contains both a comma and double quote, then follow rule 1 and 2
         * @return The string representation of this cell.
         */
        @Override
        public String toString(){

            return null;
        }
    }
}
