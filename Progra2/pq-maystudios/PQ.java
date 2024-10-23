public class PQ {
    public static void main(String[] args) {
        // Check if exactly two arguments are given
        if (args.length != 2) {
            System.out.println("ERROR: Bitte genau zwei Argumente angeben");
            return;
        }

        try {
            // Parse the first and second arguments as doubles
            double p = Double.parseDouble(args[0]);
            double q = Double.parseDouble(args[1]);

            // Calculate the discriminant 
            double discriminant = Math.pow(p / 2, 2) - q;

            // Check the value of the discriminant to determine the number of roots
            if (discriminant < -1e-7) {
                System.out.println("Es gibt 0 Nullstellen.");
            } else if (Math.abs(discriminant) <= 1e-7) {
                // Calculate and print the single root
                double root = -p / 2;
                System.out.println(root);
                System.out.println("Es gibt 1 Nullstelle.");
            } else {
                // Calculate and print the two roots
                double root1 = -p / 2 - Math.sqrt(discriminant);
                double root2 = -p / 2 + Math.sqrt(discriminant);
                System.out.println(root1);
                System.out.println(root2);
                System.out.println("Es gibt 2 Nullstellen.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Bitte gültige Zahlen als Argumente angeben");
        }
    }
}
