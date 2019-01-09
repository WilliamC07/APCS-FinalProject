import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class HandleCommand {
    private final CSVRepresentation csvRepresentation;
    public HandleCommand(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    /**
     * Sets the value of a cell to the value given by the user
     * @param column The column where the user wants the new value
     * @param row    The row where the user wants the new value
     * @param value  The new value the user wants
     */
    public void set(int column, int row, String value){
      Command c = new Command(csvRepresentation.getValue(column,row), value, column, row);
      csvRepresentation.pushCommand(c);
    }

    /**
     * Swaps the values of 2 cells provided by the user
     * @param columnOld The column of first value
     * @param rowOld    The row of the first value
     * @param columnNew The column of the second value
     * @param rowNew    The row of the second value
     */
    public void swap(int columnOld, int rowOld, int columnNew, int rowNew){
      //This string keeps track of the value of the first cell
      String oldval = csvRepresentation.getValue(columnOld,rowOld);
      //This string keeps track of the value of the second cell
      String newval = csvRepresentation.getValue(columnNew, rowNew);
      //Processes and pushes the commands
      Command c = new Command(oldval, newval, columnNew , rowNew);
      Command d = new Command(newval, oldval, columnOld, rowOld);
      csvRepresentation.pushCommand(c);
      csvRepresentation.pushCommand(d);
    }

    /**
     * Adds the values of the given cells
     * @param cellParts An integer array where the even indexes contains the columns of the cell and the odd indexes contain the rows of the cells
     * @param storeCol  The column of the cell where the user wants the sum stored
     * @param storeRow  The row of the cell where the user wants the sum stored
     */
    public void add(int[] cellParts, int storeCol, int storeRow){
      int sum = 0;
      //keeps track of the old value of the cell where the sum will be stored
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //loops through the array and the i+= 2 makes sure i is the column of the cell
      for(int i = 0; i < cellParts.length; i+= 2){
        //adds the value of the cell using i as the column and i+1 as the row of the cell
        sum += Integer.parseInt(csvRepresentation.getValue(i, i+1));
      }
      //converts the sum into a string
      String s = String.valueOf(sum);
      Command c = new Command(oldval, s , storeCol, storeRow);
    }
  }
