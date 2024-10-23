public class Newton {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("ERROR: ungültiges Argument");
            return;
        }

        double c = Double.parseDouble(args[0]);

        if (c < 0) {
            System.out.println("ERROR: ungültiges Argument");
            return;
        }

        double t = c;
        while (Math.abs(t * t - c) > 0.0001) {
            t = (t + c / t) / 2;
        }

        System.out.println(t);
    }

}
