public class Cylinder {

    public static void main(String[] args) {
        if (args.length % 2 != 0) {
            System.out.println("Invalid number of arguments");
            return;
        }

        Cylinder[] cylinders = new Cylinder[args.length / 2];

        try {
            for (int i = 0; i < args.length; i += 2) {
                double radius = Double.parseDouble(args[i]);
                double height = Double.parseDouble(args[i + 1]);
                cylinders[i / 2] = new Cylinder(radius, height);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument");
            return;
        }

        Cylinder[] sortedCylinders = Cylinder.sorted(cylinders);

        for (Cylinder cylinder : sortedCylinders) {
            System.out.println(cylinder);
        }
    }

    private double radius = 0.0;
    private double height = 0.0;

    public Cylinder() {
    }

    public Cylinder(double radius, double height) {
        this.radius = radius;
        this.height = height;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double volume() {
        return Math.PI * radius * radius * height;
    }

    public String toString() {
        return "Cylinder: r=" + radius + ", h=" + height + ", V=" + volume();
    }

    public static Cylinder[] sorted(Cylinder[] cylinders) {
        Cylinder[] sortedCylinders = cylinders.clone();
        return quickSort(sortedCylinders, 0, cylinders.length - 1);
    }

    private static Cylinder[] quickSort(Cylinder[] cylinders, int left, int right) {
        if (left < right) {
            int index = partition(cylinders, left, right);
            quickSort(cylinders, left, index - 1);
            quickSort(cylinders, index + 1, right);
        }

        return cylinders;
    }

    private static int partition(Cylinder[] cylinders, int left, int right) {
        double pivot = cylinders[right].volume();
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (cylinders[j].volume() <= pivot) {
                i++;
                Cylinder temp = cylinders[i];
                cylinders[i] = cylinders[j];
                cylinders[j] = temp;
            }
        }

        Cylinder temp = cylinders[i + 1];
        cylinders[i + 1] = cylinders[right];
        cylinders[right] = temp;

        return i + 1;
    }

}
