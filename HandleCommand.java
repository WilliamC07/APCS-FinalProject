import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

public class HandleCommand {
    private final CSVRepresentation csvRepresentation;
    public HandleCommand(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    public void handle(String command){
        String[] elements = command.split(" ");
        // Make it upper case for easier comparison
        switch(elements[0].toUpperCase()){
            case "SET":
            {
                set(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), elements[3]);
            }
                break;
            case "SWAP":
            {
              swap(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),Integer.parseInt(elements[4]));
            }
                break;
            case "ADD":
            {
              add(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),Integer.parseInt(elements[6]));
            }
                break;
            case "MULTIPLY":
            {
              multiply(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),Integer.parseInt(elements[6]));
            }
                break;
            case "DIVIDE":
            {
              division(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),Integer.parseInt(elements[4]),Integer.parseInt(elements[5]),Integer.parseInt(elements[6]));
            }
                break;
            default:
                // Don't know what kind of command, TODO: Tell the user
        }
    }

    /**
     * Sets the value of a cell to the value given by the user
     * @param column The column where the user wants the new value
     * @param row    The row where the user wants the new value
     * @param value  The new value the user wants
     */
    private void set(int column, int row, String value){
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
    private void swap(int columnOld, int rowOld, int columnNew, int rowNew){
      //This string keeps track of the value of the first cell
      String oldval = csvRepresentation.getValue(columnOld,rowOld);
      //This string keeps track of the value of the second cell
      String newval = csvRepresentation.getValue(columnNew, rowNew);
      //Processes and pushes the commands
      Command c = new Command(oldval, newval, columnOld , rowOld);
      Command d = new Command(newval, oldval, columnNew, rowNew);
      csvRepresentation.pushCommand(c);
      csvRepresentation.pushCommand(d);
    }

    /**
     * Adds the 2 given cells and stores value in third given cell
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     */
    private void add(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell where the sum will be stored
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      int sum = Integer.parseInt(num1) + Integer.parseInt(num2);
      String s = sum + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }

    private void multiply(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      double product = (double)Integer.parseInt(num1) * (double)Integer.parseInt(num2);
      String s = product + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }

    private void division(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      double quotient = (double)Integer.parseInt(num1) / (double)Integer.parseInt(num2);
      String s = quotient + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }


  }
