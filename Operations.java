public class Operations{

  public static int add(int [] values){
    int sum = 0;
    for (int i = 0; i < values.length; i++){
      sum += values[i];
    }
    return sum;
  }

  public static int range(int[] values){
    return max(values) - min(values);
  }

  public static int max(int[] values){
    int largest = 0;
    for (int i = 0; i < values.length; i++){
      if (values[i] > largest){
        largest = values[i];
      }
    }
    return largest;
  }

  public static int min(int[] values){
    int smallest = 1000000000;
    for (int i = 0; i < values.length; i++){
      if (values[i] < smallest){
        smallest = values[i];
      }
    }
    return smallest;
  }

/*  Has to finish this
  public static int mode(int[] values){
    int count = 0
  }

  All methods remaining
+ mode(int[] values): int
+ mean(int[] values): int
+ median(int[] values): int
+ sort(int[] values): int[]
+ count(int[] value, target): int
+ product(int[] values): int
*/



}
