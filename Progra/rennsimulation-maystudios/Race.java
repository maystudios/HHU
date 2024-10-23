import java.util.Scanner;

public class Race {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Racecar car = new Racecar();

        while (scanner.hasNext()) {
            String command = scanner.next();
            double value = scanner.nextDouble();

            switch (command.toLowerCase()) {
                case "accelerate":
                    car.accelerate(value);
                    break;
                case "decelerate":
                    car.decelerate(value);
                    break;
                case "drive":
                    car.drive(value);
                    break;
                default:
                    System.out.println("Unrecognized command.");
                    continue;
            }

            System.out.println(car);
        }

        scanner.close();
    }
}
