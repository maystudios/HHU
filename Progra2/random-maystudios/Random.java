public class Random {
    public static void main(String[] args) {
        // Checking if exactly two arguments are provided
        if (args.length != 2) {
            System.out.println("Bitte geben Sie genau zwei Argumente ein: die minimale und die maximale Augenzahl des Wuerfels.");
            return;
        }

        // Parsing the arguments into integers
        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);

        // Checking if the maximum value is smaller than the minimum value
        if (max < min) {
            System.out.println("Das zweite Argument darf nicht kleiner als das erste sein.");
            return;
        }

        // Generating a random number within the specified range
        int result = getRandomNumber(min, max);

        // Printing the result
        System.out.println(result);
    }

    // Method to generate a random number within a given range
    private static int getRandomNumber(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
