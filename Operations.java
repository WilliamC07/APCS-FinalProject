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

  public static int mode(int[] values){
   int maxValue = 0;
   int maxCount = 0;
   for (int i = 0; i < values.length; ++i){
       int count = 0;
       for (int j = 0; j < values.length; ++j){
           if (values[j] == values[i])
               count++;
       }
       if (count > maxCount) {
           maxCount = count;
           maxValue = values[i];
       }
   }
   return maxValue;
  }

  public static double mean(int[] values){
    return  add(values) / (double) values.length;
  }

  public static double median(int[] values){
    sort(values);
    if (values.length % 2 == 0){
      return (double)(values[values.length/2 - 1 ] + values[(values.length/2)]) / 2;
    }
    return (double)values[(values.length/2)];
  }

  public static int[] sort(int[] values){
    int before,after;
    for (int i = 0;i < values.length; i++){
      for (int x = 0; x < values.length - i - 1; x++){
        //checks 2 adjacent numbers
        if (values[x] > values[x+1]){
          //swaps if unsorted
          before = values[x];
          after = values[x+1];
          values[x] = after;
          values[x+1] = before;
        }
      }
    }
    return values;
  }

  public static int count(int[] values, int target){
    int count = 0;
    for (int i =0; i < values.length; i++){
      if (values[i] == target){
        count++;
      }
    }
    return count;
  }

  public static int product(int[] values){
    int product = 0;
    for (int i = 0; i < values.length; i++){
      product *= values[i];
    }
    return product;
  }

}
