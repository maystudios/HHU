public class Studi {

    String name;
    int[] punkte;

    public Studi(String name, int[] punkte) {
        this.name = name;
        this.punkte = punkte;
    }

    int punkteFürTeil(boolean erstesHalbjahr) {
        int p = 0;
        int start = erstesHalbjahr ? 0 : punkte.length / 2;
        int ende = erstesHalbjahr ? punkte.length / 2 : punkte.length;
        for (int i = start; i < ende; i++) {
            p += punkte[i];
        }
        return p;
    }

    boolean hatTeilZulassung(boolean teil1, double maxPunkte) {
        return punkteFürTeil(teil1) >= maxPunkte * 0.2;
    }

    boolean hatGesamtzulassung(double maxPunkte) {
        int gesamtPunkte = 0;
        for (int punkt : punkte) {
            gesamtPunkte += punkt;
        }
        return gesamtPunkte >= maxPunkte * 0.5
                && hatTeilZulassung(true, maxPunkte)
                && hatTeilZulassung(false, maxPunkte);
    }
}
