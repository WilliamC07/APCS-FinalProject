public class Screen{

  /**
   * A cell can only fit a certain amount of text, so we have to cut off parts that do not fit
   * @param  value   The string you are attempting to add
   * @param  spacing The amount of space you have in the cell
   * @return         A new string that can fit in the cell
   */
  	public String fitSpace(String value, int spacing){
      return value.substring(0,spacing);
    }
    
}
