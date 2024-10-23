public class Plateau {
    public static void main(String[] args) {
        // Überprüfe, ob mindestens 3 Werte übergeben wurden
        if (args.length < 3) {
            System.out.println("ERROR: Bitte mindestens 3 Zahlen übergeben!");
            return;
        }

        try {
            int[] values = new int[args.length];

            // Konvertiere die übergebenen Werte in int
            for (int i = 0; i < args.length; i++) {
                values[i] = Integer.parseInt(args[i]);
            }

            // Initialisiere Variablen für die Länge und den Index des längsten Plateaus
            int maxLength = 0;
            int maxIndex = -1;
            int currentLength = 0;
            int currentValue = values[0];
            boolean inPlateau = false;

            // Iteriere durch die Werte und suche das längste Plateau
            for (int i = 1; i < values.length; i++) {
                if (values[i] == currentValue) {
                    currentLength++;
                    inPlateau = true;
                } else {
                    currentValue = values[i];
                    if (inPlateau && currentLength > maxLength) {
                        maxLength = currentLength;
                        maxIndex = i - currentLength;
                    }
                    currentLength = 1;
                    inPlateau = false;
                }
            }

            // Überprüfe das letzte Plateau
            if (inPlateau && currentLength > maxLength) {
                maxLength = currentLength;
                maxIndex = values.length - currentLength;
            }

            // Gib das längste Plateau aus
            if (maxLength > 1) {
                System.out.println(maxIndex + " " + maxLength);
            } else {
                System.out.println("kein plateau");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Bitte geben Sie nur gültige Ganzzahlen ein.");
        }
    }
}
