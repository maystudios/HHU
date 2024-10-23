import java.util.Scanner;

public class Zulassung {

    public static void main(String[] args) {
        int gesamtpunktzahl = Integer.parseInt(args[0]);
        Studi[] studis = new Studi[100];
        Scanner stdin = new Scanner(System.in);
        int anzahlStudis = 0;

        while (stdin.hasNext() && anzahlStudis < 100) {
            String zeile = stdin.nextLine();
            String[] parts = zeile.split(",");
            String name = parts[0];
            int[] punkte = new int[parts.length - 1];
            for (int i = 0; i < punkte.length; i++) {
                punkte[i] = Integer.parseInt(parts[i + 1]);
            }
            studis[anzahlStudis++] = new Studi(name, punkte);
        }

        for (Studi studi : studis) {
            if (studi != null) {
                boolean teil1 = studi.hatTeilZulassung(true, gesamtpunktzahl);
                boolean teil2 = studi.hatTeilZulassung(false, gesamtpunktzahl);
                boolean gesamt = studi.hatGesamtzulassung(gesamtpunktzahl);
                System.out.println(studi.name + "," + teil1 + "," + teil2 + "," + gesamt);
            }
        }
    }
}
