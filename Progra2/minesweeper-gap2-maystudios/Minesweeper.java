import java.util.Arrays;

public class Minesweeper {

    private static int getNumbersOfMines(int[][] neighborhood) {
        return Arrays.stream(neighborhood).flatMapToInt(Arrays::stream).map(n -> n == -1 ? 1 : 0).sum();
    }

    public static void main(String[] args) {
        // Prüfe, ob Feldgröße angegeben ist
        if (args.length < 2) {
            System.out.println("ERROR: Bitte Feldgröße angeben");
            return;
        }

        // Feldgröße einlesen
        int numberOfColumns = Integer.parseInt(args[0]);
        int numberOfRows = Integer.parseInt(args[1]);

        int expectedNumberOfFields = numberOfColumns * numberOfRows;
        /*
         * F: Warum wird im folgenden Statement 2 subtrahiert?
         * A: Da wir als erstes 2 Argumente übergeben die die größe des Spielfeldes
         * festlegen
         * und dann die einzelnen Felder angeben. Nun übergeben wir dementsprechend bei
         * einem
         * 5 * 5 Feld, 25 Felder und zwei Argumente die für die größe stehen, also
         * insgesamt
         * 27 Argumente. Nun soll das Programm aber überprüfen ob 25 Felder Daten
         * angeben wurde,
         * also betrachten wir ausschließlich die Anzahl an Argumenten abzüglich der
         * größen Argumente.
         */
        int numberOfFieldsGiven = args.length - 2;
        if (expectedNumberOfFields != numberOfFieldsGiven) {
            System.out.println("ERROR: " + expectedNumberOfFields + " Felder erwartet, aber " + numberOfFieldsGiven
                    + " angegeben.");
            return;
        }

        // Eingabe in 2D-Array einlesen
        int[][] mines = new int[numberOfRows][numberOfColumns];

        for (int columnNumber = 0; columnNumber < numberOfColumns; columnNumber++) {
            for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                // berechne den Index, an dem das Element für die entsprechende Zeile/Spalte ist
                int index = rowNumber * numberOfColumns + columnNumber + 2;
                mines[rowNumber][columnNumber] = Integer.parseInt(args[index]);
            }
        }

        // Wir müssen insgesamt 8 Felder um das aktuelle Feld an (x,y) betrachten.
        // Diese 8 Felder liegen an (x-1,y-1), (x-1,y+0) usw.
        // Die zu addierenden Terme (Offsets, also (-1, -1), (-1,+0) usw.) speichern wir
        // in diesem Array.
        // (Alternativ kann man auch explizit 8 if-Abfragen formulieren.)
        int[][] offsets = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        // 2D-Array für Nachbarschaftszahlen
        int[][] neighborhood = new int[numberOfRows][numberOfColumns];
        // Betrachte jedes Feld aus dem mines-Array:
        for (int columnNumber = 0; columnNumber < numberOfColumns; columnNumber++) {
            for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                /*
                 * F: Welche Bedeutung hat es, wenn im mines-Array ein Eintrag den Wert 1 hat?
                 * A: Es ist eine Mine an dieser Position.
                 */
                if (mines[rowNumber][columnNumber] == 1) {
                    neighborhood[rowNumber][columnNumber] = -1;
                } else {
                    int numberOfMinesInNeighborhood = 0;
                    // betrachte alle 8 benachbarten Felder mithilfe des Offset-Arrays
                    for (int[] offset : offsets) {
                        // berechne zu betrachtende Spalte/Zeile
                        int column = columnNumber + offset[0];
                        int row = rowNumber + offset[1];
                        /*
                         * F: Was würde beim Ausführen des Programms (potentiell) passieren, wenn wir
                         * die folgende if-Verzweigung (also Zeile 64 und die Klammer in Zeile 69)
                         * entfernen?
                         * A: Es würde eine ArrayIndexOutOfBoundsException geworfen werden, da wir auf
                         * ein Feld zugreifen wollen, welches nicht existiert.
                         */
                        if (column >= 0 && column < numberOfColumns && row >= 0 && row < numberOfRows) {
                            // wenn wir eine Mine an der Position haben: hochzählen
                            if (mines[row][column] == 1) {
                                numberOfMinesInNeighborhood += 1;
                            }
                        }
                    }
                    // speichere berechnete Anzahl
                    neighborhood[rowNumber][columnNumber] = numberOfMinesInNeighborhood;
                }
            }
        }

        // gib das Ergebnis aus
        for (int[] row : neighborhood) {
            for (int number : row) {
                System.out.print((number == 0) ? " " : (number == -1) ? "x" : number);
                System.out.print(" ");
            }
            // nach jeder Spielfeldzeile eine neue Zeile beginnen
            System.out.println();
        }

        System.out.println(expectedNumberOfFields + " fields, " + getNumbersOfMines(neighborhood) + " mines");
    }
}