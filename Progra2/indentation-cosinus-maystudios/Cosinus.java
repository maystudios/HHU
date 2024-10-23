public class Cosinus {
    public static void main(String[] args) {
        if (args.length < 1) {
// Fehlermeldung, wenn kein Argument übergeben wurde
            System.out.println("ERROR: Bitte eine Zahl als Argument übergeben!");
        } else {
// Erstes Argument als Kommazahl abspeichern
            double xBogenmass = Double.parseDouble(args[0]);
// Rest berechnen, der bei der ganzzahligen Division durch 2π entsteht. (Beispiel: 3.5π % 2π = 1.5π)
// (nutzt Periodizität der Cosinusfunktion)
            double kleineresX = xBogenmass % (2 * Math.PI);
// Falls die beiden x'e ungefähr gleich sind, berechnen wir ein alternatives kleineresX, da sonst der mathematische Fakt trivial ist.
// (nutzt Symmetrie der Cosinusfunktion)
            if (Math.abs(kleineresX - xBogenmass) < 0.00001) {
                kleineresX = -kleineresX;
            }
// Mathematischen Fakt ausgeben
            System.out.println("Wusstest du schon, dass der Cosinus von " + xBogenmass + " gleich dem Cosinus von " + kleineresX + " ist?");
            System.out.println("cos(" + xBogenmass + ") = " + Math.cos(xBogenmass));
            System.out.println("cos(" + kleineresX + ") = " + Math.cos(kleineresX));
        }
    }
}
