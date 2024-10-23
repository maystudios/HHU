// Resistor interface
interface Resistor {
    double resistance();

    int resistorCount();
}

// SingleResistor class
class SingleResistor implements Resistor {
    private double resistance;

    public SingleResistor(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public double resistance() {
        return resistance;
    }

    @Override
    public int resistorCount() {
        return 1;
    }
}

// SeriesCircuit class
class SeriesCircuit implements Resistor {
    private Resistor r1, r2;

    public SeriesCircuit(Resistor r1, Resistor r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public double resistance() {
        return r1.resistance() + r2.resistance();
    }

    @Override
    public int resistorCount() {
        return r1.resistorCount() + r2.resistorCount();
    }
}

// ParallelCircuit class
class ParallelCircuit implements Resistor {
    private Resistor r1, r2;

    public ParallelCircuit(Resistor r1, Resistor r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public double resistance() {
        double r1Resistance = r1.resistance();
        double r2Resistance = r2.resistance();
        return (r1Resistance * r2Resistance) / (r1Resistance + r2Resistance);
    }

    @Override
    public int resistorCount() {
        return r1.resistorCount() + r2.resistorCount();
    }
}