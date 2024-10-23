import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Vocabulary {

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(",");
                System.out.println(words[0] + " " + permutiert(words[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String permutiert(String wort) {
        List<Character> characters = new ArrayList<>();
        for (char c : wort.toCharArray()) {
            characters.add(c);
        }

        String shuffled;
        do {
            Collections.shuffle(characters);
            StringBuilder sb = new StringBuilder();
            for (char c : characters) {
                sb.append(c);
            }
            shuffled = sb.toString();
        } while (shuffled.equals(wort));

        return shuffled;
    }
}
