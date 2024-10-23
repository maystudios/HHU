
// Example usage in a main method
public class Resistors {
    public static void main(String[] args) {
        Resistor r1 = new SingleResistor(100);
        Resistor r2 = new SingleResistor(200);
        Resistor r3 = new SingleResistor(300);

        Resistor r23 = new ParallelCircuit(r2, r3);
        Resistor r123 = new SeriesCircuit(r1, r23);

        System.out.println("Total Resistance: " + r123.resistance() + " Ohm");
        System.out.println("Total Resistor Count: " + r123.resistorCount());
    }
}