public class Skytale {

    private static final int ROWS = 4;
    private static final int COLS = 7;

    // Converts a string to a 2D char array, written row-wise
    private static char[][] zeilenweiseInRasterSchreiben(String message) {
        char[][] grid = new char[ROWS][COLS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = index < message.length() ? message.charAt(index) : ' ';
                index++;
            }
        }
        return grid;
    }

    // Reads a 2D char array column-wise and returns a string
    private static String rasterSpaltenweiseAblesen(char[][] grid) {
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < COLS; j++) {
            for (int i = 0; i < ROWS; i++) {
                result.append(grid[i][j]);
            }
        }
        return result.toString();
    }

    // Encrypts a message
    private static String verschluesseln(String message) {
        return rasterSpaltenweiseAblesen(zeilenweiseInRasterSchreiben(message));
    }

    // Converts a string to a 2D char array, written column-wise
    private static char[][] spaltenweiseInRasterSchreiben(String message) {
        char[][] grid = new char[ROWS][COLS];
        int index = 0;
        for (int j = 0; j < COLS; j++) {
            for (int i = 0; i < ROWS; i++) {
                grid[i][j] = index < message.length() ? message.charAt(index) : ' ';
                index++;
            }
        }
        return grid;
    }

    // Reads a 2D char array row-wise and returns a string
    private static String rasterZeilenweiseAblesen(char[][] grid) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                result.append(grid[i][j]);
            }
        }
        return result.toString();
    }

    // Decrypts a message
    private static String entschluesseln(String message) {
        return rasterZeilenweiseAblesen(spaltenweiseInRasterSchreiben(message));
    }

    // Main method to process command line arguments and perform
    // encryption/decryption
    public static void main(String[] args) {
        if (args.length != 2 || !(args[0].equals("E") || args[0].equals("D")) || args[1].length() != ROWS * COLS) {
            System.out.println("ERROR");
            return;
        }

        String operation = args[0];
        String message = args[1];
        String result;

        if (operation.equals("E")) {
            result = verschluesseln(message);
        } else {
            result = entschluesseln(message);
        }

        System.out.println(result);
    }
}
