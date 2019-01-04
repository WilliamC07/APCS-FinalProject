import java.util.Arrays;

public class Operations{

    /**
     *
     * @param values An integer array
     * @return The sum of all the elements in the array
     */
    public static int add(int [] values){
        int sum = 0;
        for (int i = 0; i < values.length; i++){
            sum += values[i];
        }
        return sum;
    }

    /**
     *
     * @param values An integer array
     * @return The difference between the largest and smallest number
     */
    public static int range(int[] values){
        return max(values) - min(values);
    }

    /**
     *
     * @param values An integer array
     * @return The largest number in the array
     */
    public static int max(int[] values){
        int largest = 0;
        for (int i = 0; i < values.length; i++){
            if (values[i] > largest){
                largest = values[i];
            }
        }
        return largest;
    }

    /**
     *
     * @param values An integer array
     * @return The smallest number in the array
     */
    public static int min(int[] values){
        int smallest = 1000000000;
        for (int i = 0; i < values.length; i++){
            if (values[i] < smallest){
                smallest = values[i];
            }
        }
        return smallest;
    }

    /**
     *
     * @param values An integer array
     * @return The mode of the array
     */
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

    /**
     *
     * @param values An integer array
     * @return The mean of the given array
     */
    public static double mean(int[] values){
        return  add(values) / (double) values.length;
    }

    /***
     *
     * @param values An integer array
     * @return The median of the array
     */
    public static double median(int[] values){
        sort(values);
        if (values.length % 2 == 0){
            return (double)(values[values.length/2 - 1 ] + values[(values.length/2)]) / 2;
        }
        return (double)values[(values.length/2)];
    }

    /**
     *
     * @param values An integer array
     * @return The given array sorted
     */
    public static int[] sort(int[] values){
        Arrays.sort(values);
        return values;
    }

    /**
     *
     * @param values An integer array
     * @param target The number the user wants to find
     * @return The number of times the target number appears in the array
     */
    public static int count(int[] values, int target){
        int count = 0;
        for (int i =0; i < values.length; i++){
            if (values[i] == target){
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param values An integer array
     * @return The product of all the numbers in the array
     */
    public static int product(int[] values){
        int product = 0;
        for (int i = 0; i < values.length; i++){
            product *= values[i];
        }
        return product;
    }

}
