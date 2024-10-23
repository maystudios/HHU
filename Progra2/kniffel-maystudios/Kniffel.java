import java.util.Arrays;
import java.util.stream.IntStream;

// Die Methoden für jede Kategorie erhält eine kurze Erklärung.
// Hinter @param steht, welche Parameter die Methode bekommt. Bei uns ist das immer "gewürfelte Augenzahlen, aufsteigend sortiert".
// Hinter @return steht, wie der Rückgabewert berechnet werden soll.

public class Kniffel {

    private static int eyeNumber(int[] dice, int eye) {
        return Arrays.stream(dice).map(n -> n == eye ? 1 : 0).sum();
    }

    private static int numberOfStreet(int[] dice) {
        dice = Arrays.stream(dice).distinct().toArray(); // remove duplicates
        int longestStreet = 0;
        int currentStreet = 1;

        for (int i = 0; i < dice.length - 1; i++) {
            if (dice[i] + 1 == dice[i + 1]) {
                currentStreet++;
            } else {
                if (currentStreet > longestStreet) {
                    longestStreet = currentStreet;
                }
                currentStreet = 1; // reset for the next sequence
            }
        }

        // Check one last time in case the longest street is at the end of the array
        if (currentStreet > longestStreet) {
            longestStreet = currentStreet;
        }

        return longestStreet;
    }

    private static int nOfKind(int[] dice, int min) {
        int maxSameEyes = 0;
        for (int i = 1; i <= 6; i++) {
            maxSameEyes = Math.max(maxSameEyes, eyeNumber(dice, i));
        }
        return (maxSameEyes >= min) ? Arrays.stream(dice).sum() : 0;
    }

    /**
     * Einser
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 1 zeigen
     */
    private static int aces(int[] dice) {
        return eyeNumber(dice, 1) * 1;
    }

    /**
     * Zweier
     * Achtung: Multiplikation mit der Augenzahl nicht vergessen: 1 2 2 2 3 gibt
     * 2×3=6 Punkte.
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 2 zeigen
     */
    private static int twos(int[] dice) {
        return eyeNumber(dice, 2) * 2;
    }

    /**
     * Dreier
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 3 zeigen
     */
    private static int threes(int[] dice) {
        return eyeNumber(dice, 3) * 3;
    }

    /**
     * Vierer
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 4 zeigen
     */
    private static int fours(int[] dice) {
        return eyeNumber(dice, 4) * 4;
    }

    /**
     * Fünfer
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 5 zeigen
     */
    private static int fives(int[] dice) {
        return eyeNumber(dice, 5) * 5;
    }

    /**
     * Sechser
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return die Summe der Augenzahlen aller Würfel, die eine 6 zeigen
     */
    private static int sixes(int[] dice) {
        return eyeNumber(dice, 6) * 6;
    }

    /**
     * Dreierpasch
     * Anmerkung: Jeder Viererpasch ist auch ein Dreierpasch.
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return Summe aller Augenzahlen, wenn mind. 3 gleiche Zahlen vorhanden,
     *         ansonsten 0 Punkte
     */
    private static int threeOfAKind(int[] dice) {
        return nOfKind(dice, 3);
    }

    /**
     * Viererpasch
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return Summe aller Augenzahlen, wenn mind. 4 gleiche Zahlen vorhanden,
     *         ansonsten 0 Punkte
     */
    private static int fourOfAKind(int[] dice) {
        return nOfKind(dice, 4);
    }

    /**
     * Full House
     * Anmerkung: Ein Kniffel zählt nicht als Full House.
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return 25 Punkte bei drei gleichen und zwei gleichen Zahlen, ansonsten 0
     *         Punkte
     */
    private static int fullHouse(int[] dice) {
        Boolean three = false;
        Boolean two = false;
        for (int i = 1; i <= 6; i++) {
            if (eyeNumber(dice, i) == 3)
                three = true;

            if (eyeNumber(dice, i) == 2)
                two = true;
        }
        return (three && two) ? 25 : 0;
    }

    /**
     * Kleine Straße
     * Beispiel: Aus den Würfeln 1, 2, 2, 3, 4 lässt sich die kleine Straße 1, 2, 3,
     * 4 bilden.
     * Gegenbsiepiel: Aus den Würfeln 1, 2, 4, 5, 6 und 1, 2, 2, 3, 5 lässt sich
     * keine kleine Straße bauen.
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return 30 Punkte, wenn vier (direkt) aufeinanderfolgenden Augenzahlen
     *         existieren (die fünfte Augenzahl ist beliebig), ansonsten 0 Punkte
     */
    private static int smallStraight(int[] dice) {
        return numberOfStreet(dice) >= 4 ? 30 : 0;
    }

    /**
     * Große Straße
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return 40 Punkte bei fünf (direkt) aufeinanderfolgenden Augenzahlen,
     *         ansonsten 0 Punkte
     */
    private static int largeStraight(int[] dice) {
        return numberOfStreet(dice) >= 5 ? 40 : 0;
    }

    /**
     * Kniffel
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return 50 Punkte bei fünf gleichen Zahlen, ansonsten 0 Punkte
     */
    private static int kniffel(int[] dice) {

        for (int i = 1; i <= 6; i++) {
            if (eyeNumber(dice, i) == 5)
                return 50;
        }
        return 0;
    }

    /**
     * Chance
     * 
     * @param dice gewürfelte Augenzahlen, aufsteigend sortiert
     * @return Summe aller Augenzahlen
     */
    private static int chance(int[] dice) {
        return Arrays.stream(dice).sum();
    }

    public static void main(String[] args) {
        // Ausgabe zum Testen
        // (Sie dürfen die main-Methode verändern.)

        if (args.length != 5) {
            // diesen Fehlerfall müsse Ihre Methoden also nicht mehr behandeln
            System.out.println("ERROR: nicht genau 5 Zahlen übergeben");
            return;
        }

        // übergebene Zahlen einlesen
        int[] dice = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            dice[i] = Integer.parseInt(args[i]);
            if (dice[i] < 1 || dice[i] > 6) {
                System.out.println("Error: ungültige Augenzahl " + dice[i]);
                return;
            }
        }

        // Array sortieren (machen wir noch später in der Vorlesung)
        Arrays.sort(dice);

        // Ausgaben zum Testen der Methoden
        System.out.println("Einser: " + aces(dice));
        System.out.println("Zweier: " + twos(dice));
        System.out.println("Dreier: " + threes(dice));
        System.out.println("Vierer: " + fours(dice));
        System.out.println("Fünfer: " + fives(dice));
        System.out.println("Sechser: " + sixes(dice));
        System.out.println("Dreierpasch: " + threeOfAKind(dice));
        System.out.println("Viererpasch: " + fourOfAKind(dice));
        System.out.println("Full House: " + fullHouse(dice));
        System.out.println("Kleine Straße: " + smallStraight(dice));
        System.out.println("Große Straße: " + largeStraight(dice));
        System.out.println("Kniffel: " + kniffel(dice));
        System.out.println("Chance: " + chance(dice));
    }
}