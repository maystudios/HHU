public class Reverse {
    public static void main(String[] args) {
        
        // Reverse the args Array
        for (int i = 0; i < args.length / 2; i++) {
            String temp = args[i];
            args[i] = args[args.length - 1 - i];
            args[args.length - 1 - i] = temp;
        }
        
        // Printing the reversed Array
        for (String arg : args) {
            System.out.println(arg);
        }
    }
}
