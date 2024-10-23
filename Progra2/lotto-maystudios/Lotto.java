import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lotto {
    
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("ERROR: Zu wenig Argumente übergeben!");
            return;
        }

        try {

			if(Integer.parseInt(args[0]) < 1) {
				System.out.println("ERROR: n ist zu klein");
				return;
			}

			if(Integer.parseInt(args[0]) > Integer.parseInt(args[1])) {
				System.out.println("ERROR: max ist größer als n");
				return;
			}

            int[] numbers = getNumberOfRandomNumbers(Integer.parseInt(args[0]), 0, Integer.parseInt(args[1]));

            String output = "";
            for (int i = 0; i < numbers.length; i++) {
                output += (i != numbers.length - 1) ? numbers[i] + ", " : numbers[i];
            }
            System.out.println(output);
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Bitte übergebe nur ganze Zahlen!");
        }

    }

    public static int[] getNumberOfRandomNumbers(int n, int min, int max) {
        List<Integer> randomNumberList = new ArrayList<>();

        for (int i = 1; i <= max; i++) {
            randomNumberList.add(i);
        }

        Collections.shuffle(randomNumberList);
        int[] res = randomNumberList.subList(0, n).stream().mapToInt(Integer::intValue).toArray();

        return res;
    }

}
