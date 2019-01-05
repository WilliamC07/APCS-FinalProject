public class Screen{

  /**
   * A cell can only fit a certain amount of text, so we have to cut off parts that do not fit
   * @param  value   The string you are attempting to add
   * @param  spacing The amount of space you have in the cell
   * @return         A new string that can fit in the cell
   */
  	public static String fitSpace(String value, int spacing){
      if (spacing > value.length()){
        return value;
      }
      int i = 0;
      String s = "";
      while (i < spacing){
        s += value.charAt(i);
        i++;
      }
      return s;
    }

    /*This is how it should look like on a 40 by 10 grid
                |          |          |
      ----------------------------------------
                |          |          |
      ----------------------------------------
                |          |          |
      ----------------------------------------
                |   	     |          |
      ----------------------------------------
                |          |          |
      ----------------------------------------

The cells are 10 spaces (so it can fit that many characters)*/

  public static String getTable(int columns, int rows, int cellspacing){
    String s = "";
    for (int j = 0; j < rows / 2; j++){
      for (int x = 0; x < (columns / cellspacing); x++){
        int i = 0;
        while(i < cellspacing){
          s += "_";
          i++;
        }
        if (x < (columns / cellspacing) - 1){
          s += "|";
        }
      }
      s += "\n";
    }
    return s;
  }
/*
Make sure that after each line, there is a '\n'

Make sure the last line is always a line of dashes.

For not just just spaces to separate the '|' We will later modify it to show content

*/
}
