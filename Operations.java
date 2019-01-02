public class Operations{

  public static int add(int [] values){
    int sum = 0;
    for (int i = 0; i < values.length; i++){
      sum += values[i];
    }
    return sum;
  }

}
