public class ScreenDriver{

  public static void main(String[] args) {
    System.out.println("Should return ap");
    System.out.println("Returned:" + Screen.fitSpace("apple", 2));
    System.out.println("Should return app");
    System.out.println("Returned:" + Screen.fitSpace("apple", 3));
    System.out.println("Should return ap p");
    System.out.println("Returned:" + Screen.fitSpace("ap ple", 4));
    System.out.println("Should return horse");
    System.out.println("Returned:" + Screen.fitSpace("horse", 5));

    }

}
