public class References {

    private static void swap1(int number1, int number2) {
        int oldNumber1 = number1;
        number1 = number2;
        number2 = oldNumber1;
    }

    private static void swap2(int[] array) {
        int oldFirstElement = array[0];
        array[0] = array[1];
        array[1] = oldFirstElement;
    }

    private static void swap3(int[] array1, int[] array2) {
        int[] oldArray1 = array1;
        array1 = array2;
        array2 = oldArray1;
    }

    public static void main(String[] args) {
        int n1 = 5;
        int n2 = 8;

        swap1(n1, n2);

        System.out.println(n1); // Ausgabe: 5
        System.out.println(n2); // Ausgabe: 8
        /*
         * Begründung:
         * Primitive Typen wie int werden in Java als Wert übergeben.
         * Die Methode swap1 ändert die ursprünglichen Werte von n1 und n2 nicht.
         */

        int[] v1 = { 10, 20 };

        swap2(v1);

        System.out.println(v1[0]); // Ausgabe: 20
        /*
         * Begründung:
         * Arrays werden in Java als Referenz übergeben. Die Methode swap2 ändert den
         * Inhalt des Arrays v1.
         */

        int[] v2 = { -1, 2 };
        int[] v3 = { 6, 4 };

        swap3(v2, v3);

        System.out.println(v2[0]); // Ausgabe: -1
        System.out.println(v3[0]); // Ausgabe: 6
        /*
         * Begründung:
         * Obwohl Arrays als Referenz übergeben werden, tauscht die Methode swap3 nur
         * die lokalen Referenzen von array1 und array2.
         * Die ursprünglichen Arrays v2 und v3 bleiben unverändert.
         */
    }
}
