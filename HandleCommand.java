import com.googlecode.lanterna.terminal.Terminal;
import static com.googlecode.lanterna.terminal.Terminal.Color;
import java.lang.Math;

public class HandleCommand {
    private final CSVRepresentation csvRepresentation;
    public HandleCommand(CSVRepresentation csvRepresentation){
        this.csvRepresentation = csvRepresentation;
    }

    public void handle(String command){
        String[] elements = command.split(" ");
        //trys to do the user inputs
        try{
          // Make it upper case for easier comparison
          switch(elements[0].toUpperCase()){
            //checks what the user wants to do and handles them accordingly
              case "SET":
              {
                String[] words = new String[elements.length - 2];
                for (int i = 0; i < words.length - 1; i++){
                  words[i] = elements[i+3];
                }
                set(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), words);
              }
                  break;
              case "SWAP":
              {
                swap(Integer.parseInt(elements[1]),Integer.parseInt(elements[2]),Integer.parseInt(elements[3]),Integer.parseInt(elements[4]));
              }
                  break;
              case "ADD":
              {
                int[] nums = new int[elements.length- 1];
                for (int i = 0; i < nums.length - 1; i++){
                  nums[i] = Integer.parseInt(elements[i+1]);
                }
                add(nums);
              }
                  break;
              case "SUBTRACT":
              {
                int[] nums = new int[elements.length- 1];
                for (int i = 0; i < nums.length - 1; i++){
                  nums[i] = Integer.parseInt(elements[i+1]);
                }
                subtract(nums);
              }
                  break;
              case "MULTIPLY":
              {
                int[] nums = new int[elements.length- 1];
                for (int i = 0; i < nums.length - 1; i++){
                  nums[i] = Integer.parseInt(elements[i+1]);
                }
                multiply(nums);
              }
                  break;
              case "DIVIDE":
              {
                int[] nums = new int[elements.length- 1];
                for (int i = 0; i < nums.length - 1; i++){
                  nums[i] = Integer.parseInt(elements[i+1]);
                }
                division(nums);
              }
                  break;
              case "RAISE":
              case "POWER":
              {
                power(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]));
              }
                  break;
              case "AVERAGE":
              case "MEAN":
              {
                int[] nums = new int[elements.length- 1];
                for (int i = 0; i < nums.length - 1; i++){
                  nums[i] = Integer.parseInt(elements[i+1]);
                }
                average(nums);
              }
                  break;
              case "REMOVE":
              case "RM":
              {
                  remove(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
              }
                  break;
              case "UNDO":
                  csvRepresentation.undo();
                  break;
              case "SHOW":
                  csvRepresentation.show(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]));
                  break;
              case "SETROWHEADER":
                  csvRepresentation.setRowHeader(Integer.parseInt(elements[1]));
                  break;
              case "SETCOLUMNHEADER":
                  csvRepresentation.setColumnHeader(Integer.parseInt(elements[1]));
                  break;
              case "SIZE":
                  csvRepresentation.resizeColumn(Integer.parseInt(elements[1]));
                  break;
              default:
                  // Don't know what kind of command, TODO: Tell the user
          }
        }
        //if the user inputs bad values, nothing happens
        catch(Exception e){
        }
      }

    /**
     * Sets the value of a cell to the value given by the user
     * @param column The column where the user wants the new value
     * @param row    The row where the user wants the new value
     * @param value  The new value the user wants
     */
    private void set(int column, int row, String[] value){
      //loops through an array and appends it to a string
      String val = "";
      for (int i = 0; i < value.length - 1; i++){
        val += value[i] + " ";
      }
      Command c = new Command(csvRepresentation.getValue(column,row), val, column, row);
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
/*
    /**
     * Adds the values inside 2 given cells into the 3rd cell.
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     */
    /*
    private void add(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell where the sum will be stored
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //num1 and num2 keep track of the values of the data in the cells
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      //finds the sum as an int and then converted into a string
      int sum = Integer.parseInt(num1) + Integer.parseInt(num2);
      String s = sum + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }*/

    /**
     * Adds the values of all the cells given by the user
     * @param nums An int array where the values alternate between column and row. The last 2 values represent the column and row where sum will be stored;
     */
    private void add(int[] nums){
      //keeps tracks of the sum
      int sum = 0;
      int len = nums.length;
      //tracks the old value of the cell given the last two values which signify the location of the storage cell
      String oldVal = csvRepresentation.getValue(nums[len - 2],nums[len - 1]);
      //loops through the array and adds the values
      for( int i = 1; i < len - 2; i += 2 ){
        String val = csvRepresentation.getValue(nums[i-1],nums[i]);
        sum += Integer.parseInt(val);
      }
      //Turns the sum into a string
      String s = sum + "";
      Command c = new Command(oldVal,s,nums[len - 2],nums[len - 1]);
      csvRepresentation.pushCommand(c);
    }
/*
    /**
     * Subtracts the values inside 2 given cells into the 3rd cell.
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     *//*
    private void subtract(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell where the sum will be stored
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //num1 and num2 keep track of the values of the data in the cells
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      //finds the sum as an int and then converted into a string
      int difference = Integer.parseInt(num1) - Integer.parseInt(num2);
      String s = difference + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }
*/
    /**
     * Subtracts the values of all the cells given by the user
     * @param nums An int array where the values alternate between column and row. The last 2 values represent the column and row where sum will be stored;
     */
    private void subtract(int[] nums){
      //keeps tracks of the first cell
      int difference = Integer.parseInt(csvRepresentation.getValue(nums[0],nums[1]));
      int len = nums.length;
      //tracks the old value of the cell given the last two values which signify the location of the storage cell
      String oldVal = csvRepresentation.getValue(nums[len - 2],nums[len - 1]);
      //loops through the array and subtracts the values
      for( int i = 3; i < len - 2; i += 2 ){
        String val = csvRepresentation.getValue(nums[i-1],nums[i]);
        difference -= Integer.parseInt(val);
      }
      //Turns the difference into a string
      String s = difference + "";
      Command c = new Command(oldVal,s,nums[len - 2],nums[len - 1]);
      csvRepresentation.pushCommand(c);
    }

/*
    /**
     * Multiplies the values inside 2 given cells into the 3rd cell.
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     */
    /*
    private void multiply(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell that will be replaced
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //num1 and num2 keep track of the values of the data in the cells
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      //finds the product as an int and then converted into a string
      double product = (double)Integer.parseInt(num1) * (double)Integer.parseInt(num2);
      String s = product + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }
*/
    /**
     * Multiplies the values of all the cells given by the user
     * @param nums An int array where the values alternate between column and row. The last 2 values represent the column and row where sum will be stored;
     */
    private void multiply(int[] nums){
      //product starts at one bc anything mulitplied by 1 is itself
      int product = 1;
      int len = nums.length;
      //tracks value of last cell
      String oldVal = csvRepresentation.getValue(nums[len - 2],nums[len - 1]);
      //loops through the array and multiplies the values
      for( int i = 1; i < len - 2; i += 2 ){
        String val = csvRepresentation.getValue(nums[i-1],nums[i]);
        product *= Integer.parseInt(val);
      }
      //converts the product to a string
      String s = product + "";
      Command c = new Command(oldVal,s,nums[len - 2],nums[len - 1]);
      csvRepresentation.pushCommand(c);
    }

/*
    /**
     * Divides the values inside 2 given cells into the 3rd cell.
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     *//*
    private void division(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell that will be replaced
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //num1 and num2 keep track of the values of the data in the cells
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      //finds the quotient as an int and then converted into a string
      double quotient = (double)Integer.parseInt(num1) / (double)Integer.parseInt(num2);
      String s = quotient + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }
*/
    /**
     * Divides the values of all the cells given by the user
     * @param nums An int array where the values alternate between column and row. The last 2 values represent the column and row where sum will be stored;
     */
    private void division(int[] nums){
      //keeps tracks of the first cell
      Double quotient = Double.parseDouble(csvRepresentation.getValue(nums[0],nums[1]));
      int len = nums.length;
      //tracks the old value of the cell given the last two values which signify the location of the storage cell
      String oldVal = csvRepresentation.getValue(nums[len - 2],nums[len - 1]);
      //loops through the array and Divides the values
      for( int i = 3; i < len - 2; i += 2 ){
        String val = csvRepresentation.getValue(nums[i-1],nums[i]);
        quotient /= Integer.parseInt(val);
      }
      //Turns the quotient into a string
      String s = quotient + "";
      Command c = new Command(oldVal,s,nums[len - 2],nums[len - 1]);
      csvRepresentation.pushCommand(c);
    }

    /**
     * Raises the value of the given cell to the power given by the user
     * @param col   Column of the cell
     * @param row   Row of the cell
     * @param power The power the user wants to raise the value to
     */
    private void power(int col, int row, int power){
      //keeps track of the old value
      String oldVal = csvRepresentation.getValue(col, row);
      //gets the double of the old value and raises it to the power
      double val = Double.parseDouble(oldVal);
      double newVal = Math.pow(val,power);
      //Converts new value to a string and creates the command
      String s = newVal + "";
      Command c = new Command(oldVal,s, col, row);
      csvRepresentation.pushCommand(c);
    }
/*
    /**
     * Finds the average of the values inside 2 given cells and places it in the 3rd cell.
     * @param col1     Column of first cell
     * @param row1     Row of first cell
     * @param col2     Column of second cell
     * @param row2     Row of second cell
     * @param storeCol Column of cell where data will be stored
     * @param storeRow Row of cell where data will be stored
     */
    /*
    private void average(int col1, int row1, int col2, int row2, int storeCol, int storeRow){
      //keeps track of the old value of the cell that will be replaced
      String oldval = csvRepresentation.getValue(storeCol,storeRow);
      //num1 and num2 keep track of the values of the data in the cells
      String num1 = csvRepresentation.getValue(col1,row1);
      String num2 = csvRepresentation.getValue(col2,row2);
      //finds the value as a double and then divides by 2 and converted into a string
      double average = (Double.parseDouble(num1) + Double.parseDouble(num2)) / 2;
      String s = average + "";
      Command c = new Command(oldval, s , storeCol, storeRow);
      csvRepresentation.pushCommand(c);
    }
*/
    /**
     * Averages the values of all the cells given by the user
     * @param nums An int array where the values alternate between column and row. The last 2 values represent the column and row where sum will be stored;
     */
    private void average(int[] nums){
      double sum = 0;
      int len = nums.length;
      //keeps track of the value of the last cell since that's where it'll be stored
      String oldVal = csvRepresentation.getValue(nums[len - 2],nums[len - 1]);
      //loops through the array and adds the numbers
      for( int i = 1; i <= len - 2; i += 2 ){
        String val = csvRepresentation.getValue(nums[i-1],nums[i]);
        sum += Double.parseDouble(val);
      }
      //divdes by len / 2 - 1 because one cell is represented by two parts in the array and subtract one because one cell represents the storage cell
      sum /= ((len/2) - 1);
      String s = sum + "";
      Command c = new Command(oldVal,s,nums[len - 2],nums[len - 1]);
      csvRepresentation.pushCommand(c);
    }


    /**
     * Removes the cell at the given row and column
     * @param col Column of element to be removed
     * @param row Row of the element to be removed
     */
    private void remove(int col, int row){
        String old = csvRepresentation.getValue(col, row);
        csvRepresentation.pushCommand(new Command(old, "", col, row));
    }

  }
