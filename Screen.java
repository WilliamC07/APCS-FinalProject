public class Screen{
  /**
   * A cell can only fit a certain amount of text, so we have to cut off parts that do not fit
   * @param  value   The string you are attempting to add
   * @param  spacing The amount of space you have in the cell
   * @return         A new string that can fit in the cell
   */
  	public String fitSpace(String value, int spacing){
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

  public String getTable(int columns, int rows, int cellspacing){
    String s = "";
    for (int j = 0; j < rows ; j++){
      for (int x = 0; x < columns; x++){
        int i = 0;
        if(i < cellspacing && j % 2 == 1){
          s += "-";
          i++;
        }
        if(x % cellspacing == 0 && x != 0 && j % 2 == 0){
          s += "|";
        }
        else if(j % 2 == 0 && x % cellspacing != 0){
          s+=" ";
        }
      }
        s += "\n";
    }
      return s;
  }
}