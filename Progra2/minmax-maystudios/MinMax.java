public class MinMax {

    public enum EMinMax {
        MIN,
        MAX
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("ERROR: Bitte mindestens eine Zahl als Argument übergeben!");
            return;
        }

        double[] values = getArgsAsDouble(args);

        if (values == null) {
            System.out.println("ERROR: Ungültige Eingabe! Bitte stellen Sie sicher, dass alle Argumente gültige Zahlen sind.");
            return;
        }

        double min = getMinMax(values, EMinMax.MIN);
        double max = getMinMax(values, EMinMax.MAX);

        System.out.println((int) min);
        System.out.println((int) max);

    }

    public static double[] getArgsAsDouble(String[] args) {
        double[] d = new double[args.length];

        try {
            for (int i = 0; i < args.length; i++) {
                d[i] = Double.parseDouble(args[i]);
            }
        } catch (NumberFormatException e) {
            return null; // Return null if any argument is not a valid number
        }

        return d;
    }

    public static double getMinMax(double[] values, EMinMax eMinMax) {
        if (values.length < 1) {
            return Double.NaN; // Return NaN for empty input Array
        }

        double result = values[0]; // Initialize result with the first value

        for (int i = 1; i < values.length; i++) {
            if (eMinMax == EMinMax.MIN && values[i] < result) {
                result = values[i]; // Update minimum
            } else if (eMinMax == EMinMax.MAX && values[i] > result) {
                result = values[i]; // Update maximum
            }
        }

        return result;
    }
}
