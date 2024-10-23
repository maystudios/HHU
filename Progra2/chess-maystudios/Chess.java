public class Chess {
    public static void main(String[] args) {
        // Check if argument for the size of the chessboard is valid
        if (args.length != 1) {
            System.out.println("ERROR: Bitte die Groesse des Feldes als Argument uebergeben!");
            return;
        }

        int n;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
			System.out.println("ERROR: Ungültige Eingabe! Bitte stellen Sie sicher, dass alle Argumente gültige Zahlen sind.");
            return;
        }

        // Check if the size of the chessboard is a positive number
        if (n < 1) {
            System.out.println("ERROR: Bitte gib eine positive Zahl an!");
            return;
        }

        StringBuilder chessboard = new StringBuilder();

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                // Append "*" if the sum of x and y is even, otherwise append " "
                chessboard.append((x + y) % 2 == 0 ? "*" : " ");
            }
            // Add a new line after each row
            chessboard.append(System.lineSeparator());
        }

        // Print the chessboard
        System.out.print(chessboard.toString());
    }
}
