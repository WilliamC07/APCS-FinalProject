public class OperationsDriver{

  public static void main(String[] args) {
    int[] nums = new int[]{1,3,2,4,6,5};
    int[] odd = new int[]{1,2,3,4,5};
    System.out.println("The array is : [1,3,2,4,6,5]");
    System.out.println("The sum should be 21");
    System.out.println("The add function returned:" + Operations.add(nums));
    System.out.println("The max should be 6");
    System.out.println("The max function returned:" + Operations.max(nums));
    System.out.println("The min should be 1");
    System.out.println("The min function returned:" + Operations.min(nums));
    System.out.println("The range should be 5");
    System.out.println("The range function returned:" + Operations.range(nums));
    System.out.println("The mean should be 3.5");
    System.out.println("The mean function returned:" + Operations.mean(nums));
    System.out.println("The median should be 3.5");
    System.out.println("The median function returned: " + Operations.median(nums));
    System.out.println("The median of {1,2,3,4,5} should be 3.0" );
    System.out.println("The function returned: " + Operations.median(odd));
    System.out.println("The sorted array should be [1,2,3,4,5,6]");
    Operations.sort(nums);
    String s = "[";
    for (int i = 0; i < nums.length; i++){
      if (i < nums.length -1){
        s += nums[i] + ",";
      }
      else{
        s += nums[i];
      }
    }
    s += "]";
    System.out.println("The sorted array is " + s);



  }

}
