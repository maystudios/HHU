 public class Square {
    public static void main(String[] args) {
        // Überprüfen ob ein Argument übergeben wurde
        if(args.length != 1) {
            System.err.println("Bitte geben Sie genau eine ganze Zahl als Argument ein.");
            System.exit(1);
        }

        try {
            int number = Integer.parseInt(args[0]);

            int squared = number * number;

            System.out.println(squared);
        } catch(NumberFormatException e) {
            System.err.println("Das übergebene Argument ist keine gültige ganze Zahl.");
            System.exit(1);
        }
    }
}
